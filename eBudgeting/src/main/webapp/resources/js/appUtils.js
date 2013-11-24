//Globally register console
if (typeof console === 'undefined') {
    console = { log: function() {} };
}

function reverseSortBy(sortByFunction) {
	  return function(left, right) {
	    var l = sortByFunction(left);
	    var r = sortByFunction(right);

	    if (l === void 0) return -1;
	    if (r === void 0) return 1;

	    return l < r ? 1 : l > r ? -1 : 0;
	  };
}


$.fn.slideTo = function(data, callBack, right) {
	var currentDom = this;
	
	var width = parseInt(this.css('width'));

	var transfer = $('<div class="transfer"></div>').css({
		'width' : (2 * width) + 'px'
	});
	
	
	var current = $('<div class="current"></div>').css({
		'width' : width + 'px',
		'left' : '0',
		'float' : 'left'
	}).html(this.html());
	
	
	
	var next = $('<div class="next"></div>').css({
		'width' : width + 'px',
		'left' : width + 'px',
		'float' : 'left'
	}).html(data);
	
	var animateCss;
	
	if(right == true) {
		next.css({
			'left' : '0'
		});
		
		current.css({
			'left' : width + 'px',
		});

		transfer.css({
			'margin-left' : '-' + width + 'px'			
		});
		
		transfer.append(next).append(current);

		this.html('').append(transfer);
		animateCss={
			'margin-left' : '0'	
		};
	} else {
		transfer.append(current).append(next);
		this.html('').append(transfer);
		animateCss={
			'margin-left' : '-' + width + 'px'
		};
	}
	
	
	transfer.animate(animateCss, 200, function() {
		currentDom.html(data);
		callBack();
	});
};

$.fn.slideLeft = function(data, callBack) {
	this.slideTo(data, callBack);
};

$.fn.slideRight = function(data, callBack) {
	this.slideTo(data, callBack, true);
};

Handlebars.registerHelper('indexHuman', function(index) {
	if(isNaN(index) || index == null) {
		return ""; 
	} else {
		var output = index + 1;
		return output + ".";
	}
});

Handlebars.registerHelper('formatNumber', function(number) {
	if(number==null || isNaN(number)) return "-";
	return addCommas(number);
});

Handlebars.registerHelper('formatTimeDetail', function(timeStamp){
	if(timeStamp !=null) {
		var time = moment(timeStamp);
		return time.format("DD/MM/YYYY HH:mm:ssน.");
		
	} else {
		return "";
	}
});

Handlebars.registerHelper('formatDate', function(date){
	var m = moment(date);
	if(m.isValid()){
		var year = parseInt(m.format('YYYY')) + parseInt('543');
		return m.format('D MMMM ') + year; 
	} else {
		return "";
	}
});

Handlebars.registerHelper('formatDateShort', function(date){
	var m = moment(date);
	if(m.isValid()){
		var year = parseInt(m.format('YYYY')) + parseInt('543');
		return m.format('DD MMM ') + year; 
	} else {
		return "";
	}
});

function formatThDateToISO(date) {
	if(date == null || date == '') {
		return "";
	}
	var dateStr = date.toString();
	var m = dateStr.match(/^(\d{1,2})\/(\d{1,2})\/(\d{4})$/);
	if(m) {
		date = (parseInt(m[3])-543) + "-" + m[2] + "-" + m[1];
	}
	
	return date;
}

Handlebars.registerHelper('formatDateDB', function(date){
	if(date == null || date == '') {
		return "";
	}
	var dateStr = date.toString();
	var m = dateStr.match(/^(\d{1,2})\/(\d{1,2})\/(\d{4})$/);
	if(m) {
		date = (parseInt(m[3])-543) + "-" + m[2] + "-" + m[1];
	}
	
	var m = moment(date);
	if(m.isValid()){
		var year = parseInt(m.format('YYYY')) + parseInt('543');
		return m.format('DD/MM/') + year; 
	} else {
		return "";
	}
});


function addCommas(nStr)
{
	nStr += '';
	x = nStr.split('.');
	x1 = x[0];
	x2 = x.length > 1 ? '.' + x[1] : '';
	var rgx = /(\d+)(\d{3})/;
	while (rgx.test(x1)) {
		x1 = x1.replace(rgx, '$1' + ',' + '$2');
	}
	return x1 + x2;
}

function round2Digit(num) {
	return Math.round(num * 100) / 100;
}

function changeCurrentRootFY(sel) {
	var value = sel.options[sel.selectedIndex].value;
	
	$.ajax({
		type: 'GET',
		url: appUrl('/Session/changeCurrentRootFY'),
		data: {
			newFiscalYear: value
		},
		success: function(response) {
			alert("success!");
			window.location.reload();
		}
	});	
}

function saveModel(model, success, error) {
	
	$.ajax({
		type: 'POST', 
		dataType: 'json',
		url: model.url(),
		contentType : 'application/json',
		data: JSON.stringify(model.toJSON()),
		success: success,
		error: error
	});
}

function loadReport(url) {
	showLoadingReportModal();
	//here we'll sleep for like 1.5 second to be able to show our nice UI 
	
	setTimeout(function() {
		$.fileDownload(url,{
			successCallback: function (url) {
				hideLoadingReportModal();
			},
			failCallback: function (responseHtml, url) {
				$("#showLoadingReportModal .modal-body").html("<div class='alert alert-error'>ERROR! : ไม่สามารถสร้างรายงานได้ </div>");
				$("#showLoadingReportModal .modal-body").append(responseHtml);
	        }
	    });
	}, 1500);
	
	
	
}

function showLoadingReportModal(){
	if($("div#showLoadingReportModal").length == 0) {
		// insert the modal html
		
		$("body").append("" +
				"<div id='showLoadingReportModal' class='modal hide fade'>" +
				"	<div class='modal-header'>" + 
				"	<button type='button' class='close' data-dismiss='modal'>&times;</button> " +
				"	<span style='font-weight: bold;'>แสดงรายงาน</span> "  +
				"</div> " +
				"<div class='modal-body'> "+
				"<img src='" + appUrl('/resources/graphics/loading.gif') + "'/>"+ " กรุณารอสักครู่ ระบบกำลังสร้างรายงาน... " + 
				"</div> "+
				"");
		
	} else {
		$("div#showLoadingReportModal").html("" +
				"<div id='showLoadingReportModal' class='modal hide fade'>" +
				"	<div class='modal-header'>" + 
				"	<button type='button' class='close' data-dismiss='modal'>&times;</button> " +
				"	<span style='font-weight: bold;'>แสดงรายงาน</span> "  +
				"</div> " +
				"<div class='modal-body'> "+
				"<img src='" + appUrl('/resources/graphics/loading.gif') + "'/>"+ " กรุณารอสักครู่ ระบบกำลังสร้างรายงาน... " + 
				"</div> "+
				"");
		
	}
	
	
	$('div#showLoadingReportModal').modal('show');
}

function hideLoadingReportModal(){
	$('div#showLoadingReportModal').modal('hide');
}
