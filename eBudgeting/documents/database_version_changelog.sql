-- Database Schema changelog
-- Recent changes are intentionally put at the end so we can 
-- easily replay from eariler version


-- Version 1 / initialize 
-- Modified Date: BEFORE JAN 20, 2012 
	create table APP_INFO (DB_VERSION number);
	insert into APP_INFO values (1);


    create table BGT_ALLOCATIONRECORD (
        id number(19,0) not null,
        amountAllocated number(19,0),
        IDX number(10,0),
        BUDGETTYPE_BGT_ID number(19,0),
        OBJECTIVE_PLN_OBJECTIVE_ID number(19,0),
        primary key (id)
    );

    create table BGT_BUDGETCOMMONTYPE (
        id number(19,0) not null,
        code varchar2(255 char),
        fiscalYear number(10,0),
        name varchar2(255 char),
        primary key (id)
    );

    create table BGT_BUDGETLEVEL (
        id number(19,0) not null,
        levelNumber number(10,0),
        name varchar2(255 char),
        primary key (id)
    );

    create table BGT_BUDGETPROPOSAL (
        id number(19,0) not null,
        amountAllocated number(19,0),
        amountRequest number(19,0),
        amountRequestNext1Year number(19,0),
        amountRequestNext2Year number(19,0),
        amountRequestNext3Year number(19,0),
        name varchar2(255 char),
        BUDGETTYPE_BGT_BUDGETTYPE_ID number(19,0),
        OBJECTIVE_ID number(19,0),
        ORGANIZATION_ID number(19,0),
        primary key (id)
    );

    create table BGT_BUDGETSIGNOFF (
        id number(19,0) not null,
        fiscalYear number(10,0),
        round number(10,0),
        status varchar2(255 char),
        ORGANIZATION_ID number(19,0),
        primary key (id)
    );

    create table BGT_BUDGETSIGNOFFLOG (
        id number(19,0) not null,
        round number(10,0),
        timestamp date,
        toStatus varchar2(255 char),
        USER_ID number(19,0),
        primary key (id)
    );

    create table BGT_BUDGETTYPE (
        id number(19,0) not null,
        code number(10,0),
        fiscalYear number(10,0),
        IDX number(10,0),
        lineNumber number(10,0),
        name varchar2(255 char),
        parentLevel number(10,0),
        parentPath varchar2(255 char),
        COMMONTYPE_BGT_ID number(19,0),
        BGT_BUDGETLEVEL_ID number(19,0),
        PARENT_BGT_BUDGETTYPE_ID number(19,0),
        PLN_UNIT_ID number(19,0),
        primary key (id)
    );

    create table BGT_FISCALBUDGETYPE (
        id number(19,0) not null,
        fiscalYear number(10,0),
        isMainType number(1,0),
        BUDGETTYPE_BGT_BUDGETTYPE_ID number(19,0),
        primary key (id)
    );

    create table BGT_FORMULACOLUMN (
        id number(19,0) not null,
        allocatedValue number(19,0),
        columnName varchar2(255 char),
        IDX number(10,0),
        isFixed number(1,0),
        unitName varchar2(255 char),
        value number(19,0),
        STRATEGY_BGT_STRATEGY_ID number(19,0),
        primary key (id)
    );

    create table BGT_FORMULASTRATEGY (
        id number(19,0) not null,
        fiscalYear number(10,0),
        IDX number(10,0),
        isStandardItem number(1,0),
        name varchar2(255 char),
        standardPrice number(10,0),
        COMMONTYPE_BGT_ID number(19,0),
        TYPE_BGT_BUDGETTYPE_ID number(19,0),
        UNIT_ID number(19,0),
        primary key (id)
    );

    create table BGT_OBJBUDGETPROPOSAL (
        id number(19,0) not null,
        amountAllocated number(19,0),
        amountRequest number(19,0),
        amountRequestNext1Year number(19,0),
        amountRequestNext2Year number(19,0),
        amountRequestNext3Year number(19,0),
        name varchar2(255 char),
        BUDGETTYPE_BGT_BUDGETTYPE_ID number(19,0),
        OBJECTIVE_ID number(19,0),
        ORGANIZATION_ID number(19,0),
        primary key (id)
    );

    create table BGT_OBJECTIVE_BUDGETTYPE (
        PLN_OBJECTIVE_id number(19,0) not null,
        budgetTypes_id number(19,0) not null
    );

    create table BGT_PROPOSALSTRATEGY (
        id number(19,0) not null,
        amountRequestNext1Year number(19,0),
        amountRequestNext2Year number(19,0),
        amountRequestNext3Year number(19,0),
        name varchar2(255 char),
        targetValue number(19,0),
        totalCalculatedAllocatedAmount number(19,0),
        totalCalculatedAmount number(19,0),
        FORMULASTRATEGY_ID number(19,0),
        BUDGETPROPOSAL_ID number(19,0),
        PLN_UNIT_ID number(19,0),
        primary key (id)
    );

    create table BGT_REQUESTCOLUMN (
        id number(19,0) not null,
        allocatedAmount number(10,0),
        amount number(10,0),
        COLUMN_BGT_FORMULACOLUMN_ID number(19,0),
        BGT_PROPOSALSTRATEGY_ID number(19,0),
        primary key (id)
    );

    create table BGT_RESERVEDBUDGET (
        id number(19,0) not null,
        amountReserved number(19,0),
        BUDGETTYPE_BGT_ID number(19,0),
        OBJECTIVE_PLN_OBJECTIVE_ID number(19,0),
        primary key (id)
    );

    create table HRX_ORGANIZATION (
        id number(19,0) not null,
        abbr varchar2(255 char),
        IDX number(10,0),
        name varchar2(255 char),
        PARENT_HRX_ORGANIZATION_ID number(19,0),
        primary key (id)
    );

    create table HRX_PERSON (
        id number(19,0) not null,
        firstName varchar2(255 char),
        lastName varchar2(255 char),
        WORKAT_HRX_ORGANIZATION_ID number(19,0),
        primary key (id)
    );

    create table PLN_JOIN_OBJECTIVENAME_TARGET (
        PLN_OBJECTIVENAME_id number(19,0) not null,
        targets_id number(19,0) not null
    );

    create table PLN_JOIN_OBJECTIVE_TARGET (
        forObjectives_id number(19,0) not null,
        targets_id number(19,0) not null
    );

    create table PLN_JOIN_OBJECTIVE_UNIT (
        PLN_OBJECTIVE_id number(19,0) not null,
        units_id number(19,0) not null
    );

    create table PLN_OBJECTIVE (
        id number(19,0) not null,
        code varchar2(255 char),
        fiscalYear number(10,0),
        IDX number(10,0),
        isLeaf number(1,0),
        lineNumber number(10,0),
        name varchar2(255 char),
        parentLevel number(10,0),
        parentPath varchar2(255 char),
        NAME_PLN_OBJECTIVENAME_ID number(19,0) not null,
        PARENT_PLN_OBJECTIVE_ID number(19,0),
        TYPE_PLN_OBJECTIVETYPE_ID number(19,0) not null,
        primary key (id)
    );

    create table PLN_OBJECTIVENAME (
        id number(19,0) not null,
        code varchar2(255 char),
        fiscalYear number(10,0),
        IDX number(10,0),
        name varchar2(255 char),
        TYPE_PLN_OBJECTIVETYPE_ID number(19,0) not null,
        primary key (id)
    );

    create table PLN_OBJECTIVERELATIONS (
        id number(19,0) not null,
        fiscalYear number(10,0),
        CHILD_OBJECTIVETYPE_ID number(19,0),
        OBJECTIVE_ID number(19,0),
        PARENT_OBJECTIVE_ID number(19,0),
        PARENT_OBJECTIVETYPE_ID number(19,0),
        primary key (id)
    );

    create table PLN_OBJECTIVETARGET (
        id number(19,0) not null,
        fiscalYear number(10,0),
        isSumable number(1,0),
        name varchar2(255 char),
        TARGETUNIT_ID number(19,0),
        primary key (id)
    );

    create table PLN_OBJECTIVETYPE (
        id number(19,0) not null,
        fiscalYear number(10,0),
        isLeaf number(1,0),
        name varchar2(255 char),
        parentPath varchar2(255 char),
        PARENT_PLN_OBJECTIVETYPE_ID number(19,0),
        primary key (id)
    );

    create table PLN_TARGETUNIT (
        id number(19,0) not null,
        name varchar2(255 char),
        primary key (id)
    );

    create table PLN_TARGETVALUE (
        id number(19,0) not null,
        allocatedValue number(19,0),
        requestedValue number(19,0),
        FOROBJECTIVE_ID number(19,0),
        OWNER_ORAGANIZATION_ID number(19,0),
        OBJECTIVETARGET_ID number(19,0),
        primary key (id)
    );

    create table PLN_TARGETVALUEALLOCRECORD (
        id number(19,0) not null,
        amountAllocated number(19,0),
        IDX number(10,0),
        OBJECTIVE_PLN_OBJECTIVE_ID number(19,0),
        OBJECTIVETARGET_ID number(19,0),
        primary key (id)
    );

    create table SEC_USER (
        id number(19,0) not null,
        password varchar2(255 char),
        username varchar2(255 char),
        PERSON_HRX_PERSON_ID number(19,0),
        primary key (id)
    );

    alter table BGT_ALLOCATIONRECORD 
        add constraint FKC9FE6AC1ECC9FC9B 
        foreign key (BUDGETTYPE_BGT_ID) 
        references BGT_BUDGETTYPE;

    alter table BGT_ALLOCATIONRECORD 
        add constraint FKC9FE6AC16410101F 
        foreign key (OBJECTIVE_PLN_OBJECTIVE_ID) 
        references PLN_OBJECTIVE;

    alter table BGT_BUDGETPROPOSAL 
        add constraint FK9E0702C7CCCD6CCC 
        foreign key (ORGANIZATION_ID) 
        references HRX_ORGANIZATION;

    alter table BGT_BUDGETPROPOSAL 
        add constraint FK9E0702C7C7B06F7B 
        foreign key (BUDGETTYPE_BGT_BUDGETTYPE_ID) 
        references BGT_BUDGETTYPE;

    alter table BGT_BUDGETPROPOSAL 
        add constraint FK9E0702C7304AFCAC 
        foreign key (OBJECTIVE_ID) 
        references PLN_OBJECTIVE;

    alter table BGT_BUDGETSIGNOFF 
        add constraint FKB506AFFDCCCD6CCC 
        foreign key (ORGANIZATION_ID) 
        references HRX_ORGANIZATION;

    alter table BGT_BUDGETSIGNOFFLOG 
        add constraint FK353B1A0711474C34 
        foreign key (USER_ID) 
        references SEC_USER;

    alter table BGT_BUDGETTYPE 
        add constraint FK1C85618F1176B719 
        foreign key (BGT_BUDGETLEVEL_ID) 
        references BGT_BUDGETLEVEL;

    alter table BGT_BUDGETTYPE 
        add constraint FK1C85618FCBE0BFCC 
        foreign key (PLN_UNIT_ID) 
        references PLN_TARGETUNIT;

    alter table BGT_BUDGETTYPE 
        add constraint FK1C85618F62AF6640 
        foreign key (COMMONTYPE_BGT_ID) 
        references BGT_BUDGETCOMMONTYPE;

    alter table BGT_BUDGETTYPE 
        add constraint FK1C85618F467AF66 
        foreign key (PARENT_BGT_BUDGETTYPE_ID) 
        references BGT_BUDGETTYPE;

    alter table BGT_FISCALBUDGETYPE 
        add constraint FK9F03331BC7B06F7B 
        foreign key (BUDGETTYPE_BGT_BUDGETTYPE_ID) 
        references BGT_BUDGETTYPE;

    alter table BGT_FORMULACOLUMN 
        add constraint FK62638C8CB32B5513 
        foreign key (STRATEGY_BGT_STRATEGY_ID) 
        references BGT_FORMULASTRATEGY;

    alter table BGT_FORMULASTRATEGY 
        add constraint FKE76AE3297234B1F6 
        foreign key (TYPE_BGT_BUDGETTYPE_ID) 
        references BGT_BUDGETTYPE;

    alter table BGT_FORMULASTRATEGY 
        add constraint FKE76AE32962AF6640 
        foreign key (COMMONTYPE_BGT_ID) 
        references BGT_BUDGETCOMMONTYPE;

    alter table BGT_FORMULASTRATEGY 
        add constraint FKE76AE32956DF7919 
        foreign key (UNIT_ID) 
        references PLN_TARGETUNIT;

    alter table BGT_OBJBUDGETPROPOSAL 
        add constraint FK25D5123ECCCD6CCC 
        foreign key (ORGANIZATION_ID) 
        references HRX_ORGANIZATION;

    alter table BGT_OBJBUDGETPROPOSAL 
        add constraint FK25D5123EC7B06F7B 
        foreign key (BUDGETTYPE_BGT_BUDGETTYPE_ID) 
        references BGT_BUDGETTYPE;

    alter table BGT_OBJBUDGETPROPOSAL 
        add constraint FK25D5123E304AFCAC 
        foreign key (OBJECTIVE_ID) 
        references PLN_OBJECTIVE;

    alter table BGT_OBJECTIVE_BUDGETTYPE 
        add constraint FKC2A5C0755898EDD9 
        foreign key (PLN_OBJECTIVE_id) 
        references PLN_OBJECTIVE;

    alter table BGT_OBJECTIVE_BUDGETTYPE 
        add constraint FKC2A5C0751736E6B6 
        foreign key (budgetTypes_id) 
        references BGT_BUDGETTYPE;

    alter table BGT_PROPOSALSTRATEGY 
        add constraint FKFD46D1F5CBE0BFCC 
        foreign key (PLN_UNIT_ID) 
        references PLN_TARGETUNIT;

    alter table BGT_PROPOSALSTRATEGY 
        add constraint FKFD46D1F546681FAB 
        foreign key (BUDGETPROPOSAL_ID) 
        references BGT_BUDGETPROPOSAL;

    alter table BGT_PROPOSALSTRATEGY 
        add constraint FKFD46D1F5BD677829 
        foreign key (FORMULASTRATEGY_ID) 
        references BGT_FORMULASTRATEGY;

    alter table BGT_REQUESTCOLUMN 
        add constraint FKD0F5EAF51538AD3B 
        foreign key (BGT_PROPOSALSTRATEGY_ID) 
        references BGT_PROPOSALSTRATEGY;

    alter table BGT_REQUESTCOLUMN 
        add constraint FKD0F5EAF54861E7C2 
        foreign key (COLUMN_BGT_FORMULACOLUMN_ID) 
        references BGT_FORMULACOLUMN;

    alter table BGT_RESERVEDBUDGET 
        add constraint FK3C6439FDECC9FC9B 
        foreign key (BUDGETTYPE_BGT_ID) 
        references BGT_BUDGETTYPE;

    alter table BGT_RESERVEDBUDGET 
        add constraint FK3C6439FD6410101F 
        foreign key (OBJECTIVE_PLN_OBJECTIVE_ID) 
        references PLN_OBJECTIVE;

    alter table HRX_ORGANIZATION 
        add constraint FK308EFD444335D826 
        foreign key (PARENT_HRX_ORGANIZATION_ID) 
        references HRX_ORGANIZATION;

    alter table HRX_PERSON 
        add constraint FK15E15826732A7180 
        foreign key (WORKAT_HRX_ORGANIZATION_ID) 
        references HRX_ORGANIZATION;

    alter table PLN_JOIN_OBJECTIVENAME_TARGET 
        add constraint FKBBB55AD470F62A99 
        foreign key (PLN_OBJECTIVENAME_id) 
        references PLN_OBJECTIVENAME;

    alter table PLN_JOIN_OBJECTIVENAME_TARGET 
        add constraint FKBBB55AD4586CCDB4 
        foreign key (targets_id) 
        references PLN_OBJECTIVETARGET;

    alter table PLN_JOIN_OBJECTIVE_TARGET 
        add constraint FK23318F3F109D69C2 
        foreign key (forObjectives_id) 
        references PLN_OBJECTIVE;

    alter table PLN_JOIN_OBJECTIVE_TARGET 
        add constraint FK23318F3F586CCDB4 
        foreign key (targets_id) 
        references PLN_OBJECTIVETARGET;

    alter table PLN_JOIN_OBJECTIVE_UNIT 
        add constraint FK1C028EB25898EDD9 
        foreign key (PLN_OBJECTIVE_id) 
        references PLN_OBJECTIVE;

    alter table PLN_JOIN_OBJECTIVE_UNIT 
        add constraint FK1C028EB2568FAF4E 
        foreign key (units_id) 
        references PLN_TARGETUNIT;

    alter table PLN_OBJECTIVE 
        add constraint FK23ED5AECF556C1E 
        foreign key (TYPE_PLN_OBJECTIVETYPE_ID) 
        references PLN_OBJECTIVETYPE;

    alter table PLN_OBJECTIVE 
        add constraint FK23ED5AEC264870CE 
        foreign key (PARENT_PLN_OBJECTIVE_ID) 
        references PLN_OBJECTIVE;

    alter table PLN_OBJECTIVE 
        add constraint FK23ED5AEC10F25FCD 
        foreign key (NAME_PLN_OBJECTIVENAME_ID) 
        references PLN_OBJECTIVENAME;

    alter table PLN_OBJECTIVENAME 
        add constraint FK66D27777F556C1E 
        foreign key (TYPE_PLN_OBJECTIVETYPE_ID) 
        references PLN_OBJECTIVETYPE;

    alter table PLN_OBJECTIVERELATIONS 
        add constraint FKA1CF846B24C52881 
        foreign key (PARENT_OBJECTIVETYPE_ID) 
        references PLN_OBJECTIVETYPE;

    alter table PLN_OBJECTIVERELATIONS 
        add constraint FKA1CF846B7B44221 
        foreign key (PARENT_OBJECTIVE_ID) 
        references PLN_OBJECTIVE;

    alter table PLN_OBJECTIVERELATIONS 
        add constraint FKA1CF846B304AFCAC 
        foreign key (OBJECTIVE_ID) 
        references PLN_OBJECTIVE;

    alter table PLN_OBJECTIVERELATIONS 
        add constraint FKA1CF846BF8386E4F 
        foreign key (CHILD_OBJECTIVETYPE_ID) 
        references PLN_OBJECTIVETYPE;

    alter table PLN_OBJECTIVETARGET 
        add constraint FK651DF7D7CFA5268 
        foreign key (TARGETUNIT_ID) 
        references PLN_TARGETUNIT;

    alter table PLN_OBJECTIVETYPE 
        add constraint FK66D58C266B1838AE 
        foreign key (PARENT_PLN_OBJECTIVETYPE_ID) 
        references PLN_OBJECTIVETYPE;

    alter table PLN_TARGETVALUE 
        add constraint FKAE906F739764B5AC 
        foreign key (OBJECTIVETARGET_ID) 
        references PLN_OBJECTIVETARGET;

    alter table PLN_TARGETVALUE 
        add constraint FKAE906F73FBBD47BD 
        foreign key (OWNER_ORAGANIZATION_ID) 
        references HRX_ORGANIZATION;

    alter table PLN_TARGETVALUE 
        add constraint FKAE906F73233AA3F5 
        foreign key (FOROBJECTIVE_ID) 
        references PLN_OBJECTIVE;

    alter table PLN_TARGETVALUEALLOCRECORD 
        add constraint FKE1B922539764B5AC 
        foreign key (OBJECTIVETARGET_ID) 
        references PLN_OBJECTIVETARGET;

    alter table PLN_TARGETVALUEALLOCRECORD 
        add constraint FKE1B922536410101F 
        foreign key (OBJECTIVE_PLN_OBJECTIVE_ID) 
        references PLN_OBJECTIVE;

    alter table SEC_USER 
        add constraint FK67C6D319B3B60151 
        foreign key (PERSON_HRX_PERSON_ID) 
        references HRX_PERSON;

    create sequence BGT_ALLOCATIONRECORD_SEQ;

    create sequence BGT_BUDGETCOMMONTYPE_SEQ;

    create sequence BGT_BUDGETLEVEL_SEQ;

    create sequence BGT_BUDGETPROPOSAL_SEQ;

    create sequence BGT_BUDGETSIGNOFFLOG_SEQ;

    create sequence BGT_BUDGETSIGNOFF_SEQ;

    create sequence BGT_BUDGETTYPE_SEQ;

    create sequence BGT_FISCALBUDGETYPE_SEQ;

    create sequence BGT_FORMULACOLUMN_SEQ;

    create sequence BGT_FORMULASTRATEGY_SEQ;

    create sequence BGT_OBJBUDGETPROPOSAL_SEQ;

    create sequence BGT_PROPOSALSTRATEGY_SEQ;

    create sequence BGT_REQUESTCOLUMN_SEQ;

    create sequence BGT_RESERVEDBUDGET_SEQ;

    create sequence HRX_ORGANIZATION_SEQ;

    create sequence HRX_PERSON_SEQ;

    create sequence PLN_OBJECTIVENAME_SEQ;

    create sequence PLN_OBJECTIVERELATIONS_SEQ;

    create sequence PLN_OBJECTIVETARGET_SEQ;

    create sequence PLN_OBJECTIVETYPE_SEQ;

    create sequence PLN_OBJECTIVE_SEQ;

    create sequence PLN_TARGETUNIT_SEQ;

    create sequence PLN_TARGETVALUEALLOCRECORD_SEQ;

    create sequence PLN_TARGETVALUE_SEQ;

    create sequence SEC_USER_SEQ;

    
-- version 2 
-- Modified Date: JAN 20, 2012
    update app_info set db_version=2;
    
	create table BGT_OBJBGTPROPOSALTARGET (
        id number(19,0) not null,
        targetValue number(19,0),
        BGT_OBJBGTPROPOSAL_ID number(19,0),
        PLN_TARGETUNIT_ID number(19,0),
        primary key (id)
    );
    
    alter table BGT_OBJBGTPROPOSALTARGET 
        add constraint FK86174B0B2675B7F 
        foreign key (BGT_OBJBGTPROPOSAL_ID) 
        references BGT_OBJBUDGETPROPOSAL;
    
    alter table BGT_OBJBGTPROPOSALTARGET 
        add constraint FK86174B0B5E6A86DB 
        foreign key (PLN_TARGETUNIT_ID) 
        references PLN_TARGETUNIT;

    create sequence BGT_OBJBGTPROPOSALTARGET_SEQ;
    
-- version 3
-- Modified Date: Jan 21, 2013
    update app_info set db_version=3;
    alter table BGT_BUDGETSIGNOFF add (
        lock1TimeStamp timestamp,
        lock2TimeStamp timestamp,
	    unlock1TimeStamp timestamp,
        unLock2TimeStamp timestamp,
        HRX_LOCK1PERSON_ID number(19,0),
        HRX_LOCK2PERSON_ID number(19,0),
        HRX_UNLOCK1PERSON_ID number(19,0),
        HRX_UNLOCK2PERSON_ID number(19,0)
    );
    
    alter table BGT_BUDGETSIGNOFF 
        add constraint FKB506AFFDEE824A36 
        foreign key (HRX_LOCK2PERSON_ID) 
        references HRX_PERSON;

    alter table BGT_BUDGETSIGNOFF 
        add constraint FKB506AFFDFA38D917 
        foreign key (HRX_LOCK1PERSON_ID) 
        references HRX_PERSON;

    alter table BGT_BUDGETSIGNOFF 
        add constraint FKB506AFFD89C930CF 
        foreign key (HRX_UNLOCK2PERSON_ID) 
        references HRX_PERSON;

    alter table BGT_BUDGETSIGNOFF 
        add constraint FKB506AFFD957FBFB0 
        foreign key (HRX_UNLOCK1PERSON_ID) 
        references HRX_PERSON;

    
-- version 4
-- Modified Date: Jan 29, 2013
    update app_info set db_version=4;    
    
    create table PLN_OBJECTIVEDETAIL (
        id number(19,0) not null,
        location varchar2(200 char),
        email varchar2(100 char),
        methodology1 varchar2(1000 char),
        methodology2 varchar2(1000 char),
        methodology3 varchar2(1000 char),
        officerInCharge varchar2(100 char),
        outcome varchar2(1000 char),
        output varchar2(1000 char),
        projectObjective varchar2(1000 char),
        phoneNumber varchar2(30 char),
        reason varchar2(1000 char),
        targetArea varchar2(1000 char),
        targetDescription varchar2(1000 char),
        timeframe varchar2(200 char),
        PLN_OBJECTIVE_ID number(19,0),
        HRX_ORGANIZATION_ID number(19,0),
        primary key (id)
    );
    
   	alter table PLN_OBJECTIVEDETAIL 
        add constraint FKEB3D863D67BD3B3B 
        foreign key (HRX_ORGANIZATION_ID) 
        references HRX_ORGANIZATION;

    alter table PLN_OBJECTIVEDETAIL 
        add constraint FKEB3D863D5898EDD9 
        foreign key (PLN_OBJECTIVE_ID) 
        references PLN_OBJECTIVE;
        
   	create sequence PLN_OBJECTIVEDETAIL_SEQ;
   	
-- version 5
-- Modified Date: Feb 19, 2013
    update app_info set db_version=5;    
    
   	alter table hrx_organization add (code varchar2(20));
   	
   	create table PLN_OBJECTIVEOWNERRELATION (
        id number(19,0) not null,
        OBJ_PLN_OBJECTIVE_ID number(19,0) not null,
        primary key (id)
    );
    
	alter table PLN_OBJECTIVEOWNERRELATION 
        add constraint FK8F586623D66F5021 
        foreign key (OBJ_PLN_OBJECTIVE_ID) 
        references PLN_OBJECTIVE;

	create sequence PLN_OBJOWNERRELATION_SEQ;

        
	create table PLN_OBJECTIVE_OWNER_JOIN (
        PLN_OBJECTIVEOWNERRELATION_id number(19,0) not null,
        owners_id number(19,0) not null
    );
    
    alter table PLN_OBJECTIVE_OWNER_JOIN 
        add constraint FK229966897ED7F2BF 
        foreign key (owners_id) 
        references HRX_ORGANIZATION;

    alter table PLN_OBJECTIVE_OWNER_JOIN 
        add constraint FK229966893FFF619B 
        foreign key (PLN_OBJECTIVEOWNERRELATION_id) 
        references PLN_OBJECTIVEOWNERRELATION;
 
    create table PLN_ACTIVITY (
        id number(19,0) not null,
        idx number(10,0),
        name varchar2(255 char),
        remark varchar2(255 char),
        targetValue number(19,0),
        OBJ_PLN_OBJECTIVE_ID number(19,0) not null,
        OWNER_HRX_ORGANIZATION number(19,0),
        UNIT_PLN_TARGETUNIT_ID number(19,0),
        primary key (id)
    );
    
    alter table PLN_ACTIVITY 
        add constraint FKDB204ADC7B0190A7 
        foreign key (OBJ_PLN_OBJECTIVE_ID) 
        references PLN_OBJECTIVE;

    alter table PLN_ACTIVITY 
        add constraint FKDB204ADC2067F255 
        foreign key (OWNER_HRX_ORGANIZATION) 
        references HRX_ORGANIZATION;

    alter table PLN_ACTIVITY 
        add constraint FKDB204ADC67FA70E0 
        foreign key (UNIT_PLN_TARGETUNIT_ID) 
        references PLN_TARGETUNIT;

    create sequence PLN_ACTIVITY_SEQ;

    
    
-- version 6
-- Modified Date: Feb 25, 2013
    update app_info set db_version=6;
    
    alter table PLN_ACTIVITY add (PARENT_PLN_ACTIVITY_ID number(19,0));
    
	create table PLN_ACTIVITYPERFORMANCE (
        id number(19,0) not null,
        budgetAllocated double precision,
        ACTIVITY_PLN_ACTIVITY_ID number(19,0),
        OWNER_HRX_ORGANIZATION_ID number(19,0),
        primary key (id)
    );
    
    
    alter table PLN_ACTIVITYPERFORMANCE 
        add constraint FKF6E37B4C9F5A78B 
        foreign key (ACTIVITY_PLN_ACTIVITY_ID) 
        references PLN_ACTIVITY;
        
    alter table PLN_ACTIVITYPERFORMANCE 
        add constraint FKF6E37B462A663AF 
        foreign key (OWNER_HRX_ORGANIZATION_ID) 
        references HRX_ORGANIZATION;
        
    create sequence PLN_ACTIVITYPERFORMANCE_SEQ;
    
    create table PLN_ACTIVITYTARGET (
        id number(19,0) not null,
        targetValue number(19,0),
        ACTIVITY_PLN_ACTIVITY_ID number(19,0) not null,
        UNIT_PLN_TARGETUNIT_ID number(19,0) not null,
        primary key (id)
    );
    
    
    alter table PLN_ACTIVITYTARGET 
        add constraint FK992D1B6DC9F5A78B 
        foreign key (ACTIVITY_PLN_ACTIVITY_ID) 
        references PLN_ACTIVITY;

    alter table PLN_ACTIVITYTARGET 
        add constraint FK992D1B6D67FA70E0 
        foreign key (UNIT_PLN_TARGETUNIT_ID) 
        references PLN_TARGETUNIT;
        
    create sequence PLN_ACTIVITYTARGET_SEQ;
    

    create table PLN_ACTIVITYTARGETREPORT (
        id number(19,0) not null,
        targetValue number(19,0),
        PERFORMANCE_PLN_ACTPER_ID number(19,0),
        TARGET_PLN_ACTTARGET_ID number(19,0),
        primary key (id)
    );
    
    alter table PLN_ACTIVITYTARGETREPORT 
        add constraint FKA1935EE1CE2DDF80 
        foreign key (TARGET_PLN_ACTTARGET_ID) 
        references PLN_ACTIVITYTARGET;

    alter table PLN_ACTIVITYTARGETREPORT 
        add constraint FKA1935EE135264F66 
        foreign key (PERFORMANCE_PLN_ACTPER_ID) 
        references PLN_ACTIVITYPERFORMANCE;
        
    create sequence PLN_ACTIVITYTARGETREPORT_SEQ;
        
    create table PLN_MONTHLYBGTREPORT (
        id number(19,0) not null,
        budgetPlan number(19,0),
        budgetResult number(19,0),
        fiscalMonth number(10,0),
        remark varchar2(1024 char),
        PERFORMANCE_PLN_ACTPER_ID number(19,0),
        OWNER_HRX_ORGANIZATION_ID number(19,0),
        primary key (id)
    );
    
	alter table PLN_MONTHLYBGTREPORT 
        add constraint FK2CB2142335264F66 
        foreign key (PERFORMANCE_PLN_ACTPER_ID) 
        references PLN_ACTIVITYPERFORMANCE;

    alter table PLN_MONTHLYBGTREPORT 
        add constraint FK2CB2142362A663AF 
        foreign key (OWNER_HRX_ORGANIZATION_ID) 
        references HRX_ORGANIZATION;

    create sequence PLN_MONTHLYBGTREPORT_SEQ;

        
    create table PLN_MONTHLYACTREPORT (
        id number(19,0) not null,
        activityPlan number(19,0),
        activityResult number(19,0),
        fiscalMonth number(10,0),
        remark varchar2(1024 char),
        OWNER_HRX_ORGANIZATION_ID number(19,0),
        REPORT_PLN_ACTTARGETREPORT_ID number(19,0),
        primary key (id)
    );    
    
    alter table PLN_MONTHLYACTREPORT 
        add constraint FKF8E8F1A6AE5B179D 
        foreign key (REPORT_PLN_ACTTARGETREPORT_ID) 
        references PLN_ACTIVITYTARGETREPORT;

    alter table PLN_MONTHLYACTREPORT 
        add constraint FKF8E8F1A662A663AF 
        foreign key (OWNER_HRX_ORGANIZATION_ID) 
        references HRX_ORGANIZATION;
    
    create sequence PLN_MONTHLYACTREPORT_SEQ;
        
-- version 7
-- Modified Date: March 5, 2013
    update app_info set db_version=7;
    
    alter table PLN_ACTIVITYTARGETREPORT 
    	add (OWNER_HRX_ORGANIZATION_ID number(19,0));
    	
    alter table PLN_ACTIVITYTARGETREPORT 
        add constraint FK2CB2142F6246358A 
        foreign key (OWNER_HRX_ORGANIZATION_ID) 
        references HRX_ORGANIZATION;
        
-- version 8
-- Modeified Date: March 10, 2013
	update app_info set db_version=8;

    create table PLN_ACTIVITYTARGETRESULT (
        id number(19,0) not null,
        remark varchar2(1024 char),
        removed number(1,0),
        reportedResultDate date,
        result number(19,0),
        timestamp timestamp,
        REPORTPERSON_HRX_PERSON_ID number(19,0),
        TARGETREPORT_PLN_REPORT_ID number(19,0),
        primary key (id)
    );
    
    alter table PLN_ACTIVITYTARGETRESULT 
        add constraint FKA194D1CAB697B485 
        foreign key (REPORTPERSON_HRX_PERSON_ID) 
        references HRX_PERSON;

    alter table PLN_ACTIVITYTARGETRESULT 
        add constraint FKA194D1CA1D131B41 
        foreign key (TARGETREPORT_PLN_REPORT_ID) 
        references PLN_ACTIVITYTARGETREPORT;
        
  	create sequence PLN_ACTIVITYTARGETSRESULT_SEQ;

-- version 9
-- Modified Date: March 12, 2013
	update app_info set db_version=9;
	
	alter table bgt_proposalstrategy add (
		targetvaluenext1year number(19,0), 
		targetvaluenext2year number(19,0), 
		targetvaluenext3year number(19,0)
	);
	
	alter table BGT_OBJBGTPROPOSALTARGET add(
		targetvaluenext1year number(19,0), 
		targetvaluenext2year number(19,0), 
		targetvaluenext3year number(19,0)	
	);
	
-- version 10
-- Modified Date: March 13, 2013
	update app_info set db_version=10;
	
	alter table pln_activity add (
		REGULATOR_HRX_ORGANIZATION number(19,0)
	);
	
    alter table PLN_ACTIVITY 
        add constraint FKDB204AC983D34833 
        foreign key (REGULATOR_HRX_ORGANIZATION) 
        references HRX_ORGANIZATION;

        
-- version 11
-- Modified Date : April 3, 2013
    update app_info set db_version=11;
    
    alter table PLN_ACTIVITYTARGET add (
    	BUDGETALLOCATED number(19,0)
    );
    
    alter table PLN_ACTIVITYTARGETREPORT add(
    	REPORTLEVEL number (4,0)
    );
    
-- version 12
-- Modified Date : April 16, 2013
    update app_info set db_version=12;

    create table BGT_ASSETBUDGET (
        id number(19,0) not null,
        code varchar2(255 char),
        description varchar2(255 char),
        name varchar2(255 char),
        ASSETKIND_ID number(19,0),
        primary key (id)
    );
    
	create sequence BGT_ASSETBUDGET_SEQ;

    
    create table BGT_ASSETALLOCATION (
        id number(19,0) not null,
        fiscalYear number(10,0),
        quantity number(10,0),
        unitBudget number(19,0),
        BGT_BUDGETTYPE_ID number(19,0),
        PLN_ACTIVITY_ID number(19,0),
        PLN_OBJECTIVE_ID number(19,0),
        BGT_PROPOSAL_ID number(19,0),
        BGT_ASSETBUDGET_ID number(19,0),
        HRX_OWNER_ID number(19,0),
        HRX_PARENTOWNER_ID number(19,0),
        primary key (id)
    );
    
    alter table BGT_ASSETALLOCATION 
        add constraint FKB03304201C26D3BB 
        foreign key (BGT_BUDGETTYPE_ID) 
        references BGT_BUDGETTYPE;

    alter table BGT_ASSETALLOCATION 
        add constraint FKB033042075DA5CBB 
        foreign key (PLN_ACTIVITY_ID) 
        references PLN_ACTIVITY;

    alter table BGT_ASSETALLOCATION 
        add constraint FKB98E4275BC9DF315 
        foreign key (BGT_PROPOSAL_ID) 
        references BGT_BUDGETPROPOSAL;
        
    alter table BGT_ASSETALLOCATION 
        add constraint FKB03304205898EDD9 
        foreign key (PLN_OBJECTIVE_ID) 
        references PLN_OBJECTIVE;
    
  	alter table BGT_ASSETALLOCATION 
        add constraint FKB0333887AE9F83CC 
        foreign key (BGT_ASSETBUDGET_ID) 
        references BGT_ASSETBUDGET;
        
    create sequence BGT_ASSETALLOCATION_SEQ;

    
-- version 13
-- Modified Date : April 23, 2013
 	update app_info set db_version=13;
	alter table PLN_ACTIVITYTARGET
		add (PROVINCIALTARGET NUMBER(1));

		
	alter table PLN_ACTIVITYTARGETRESULT add (
	  	RESULTBUDGETTYPE  NUMBER(1),
	  	BUDGETFISCALMONTH NUMBER(4),
	  	BUDGETRESULT      FLOAT(126)
	);
	
	alter table PLN_MONTHLYBGTREPORT modify ( 
   		BUDGETRESULT    float(126)
	);
	
	
-- version 14
-- Modified Date : May 8, 2013
	update app_info set db_version=14;
	
	alter table pln_activitytargetresult rename  column "TIMESTAMP" to "RESULTTIMESTAMP";
	
	
-- version 15
-- Modifed Date : May 18, 2013
	update app_info set db_version=15;
	
    
    create table BGT_ASSETMETHOD (
        id number(19,0) not null,
        name varchar2(255 char),
        primary key (id)
    );

    create table BGT_ASSETMETHODSTEP (
        id number(19,0) not null,
        name varchar2(255 char),
        primary key (id)
    );

    create table BGT_ASSETMETHODSTEP_JOIN (
        BGT_ASSETMETHOD_ID number(19,0) not null,
        BGT_ASSETMETHODSTEP_ID number(19,0) not null,
        STEPORDER number(10,0) not null,
        primary key (BGT_ASSETMETHOD_ID, STEPORDER)
    );
    alter table BGT_ASSETMETHODSTEP_JOIN 
        add constraint FK7AE5D7BC43248F39 
        foreign key (BGT_ASSETMETHODSTEP_ID) 
        references BGT_ASSETMETHODSTEP;

    alter table BGT_ASSETMETHODSTEP_JOIN 
        add constraint FK7AE5D7BC44BB6619 
        foreign key (BGT_ASSETMETHOD_ID) 
        references BGT_ASSETMETHOD;

   	alter table BGT_ASSETALLOCATION add (
        BGT_ASSETMETHOD_ID number(19,0),
        HRX_OPERATOR_ID number(19,0)
    );
    
    alter table BGT_ASSETALLOCATION 
        add constraint FKB033042044BB6619 
        foreign key (BGT_ASSETMETHOD_ID) 
        references BGT_ASSETMETHOD;
        
    create table BGT_ASSETSTEPREPORT (
        id number(19,0) not null,
        actualBegin date,
        actualEnd date,
        planBegin date,
        planEnd date,
        BGT_ASSETALLOCATION_ID number(19,0),
        BGT_ASSETMETHODSTEP_ID number(19,0),
        STEPORDER number(10,0),
        primary key (id)
    );
    
    alter table BGT_ASSETSTEPREPORT 
        add constraint FKF70DC6402A6DA39 
        foreign key (BGT_ASSETALLOCATION_ID) 
        references BGT_ASSETALLOCATION;

    alter table BGT_ASSETSTEPREPORT 
        add constraint FKF70DC64043248F39 
        foreign key (BGT_ASSETMETHODSTEP_ID) 
        references BGT_ASSETMETHODSTEP;
        
    create sequence BGT_ASSETMETHOD_SEQ;
    create sequence BGT_ASSETMETHODSTEP_SEQ;
    create sequence BGT_ASSETSTEPREPORT_SEQ;

insert into bgt_assetmethod( id, name) values (1, 'วิธีตกลงราคา');
insert into bgt_assetmethod( id, name) values (2, 'วิธีสอบราคา');
insert into bgt_assetmethod( id, name) values (3, 'วิธีประกวดราคา');
insert into bgt_assetmethod( id, name) values (4, 'วิธีพิเศษ');
insert into bgt_assetmethod( id, name) values (5, 'วิธีกรณีพิเศษ');
insert into bgt_assetmethod( id, name) values (6, 'วิธีประกวดราคาอิเล็กทรอนิกส์');

insert into bgt_assetmethodstep(id, name) values (1, 'ลงนามใบสั่ง/สัญญา');
insert into bgt_assetmethodstep(id, name) values (2, 'กำหนดส่งมอบ');
insert into bgt_assetmethodstep(id, name) values (3, 'ตรวจรับการส่งมอบ');
insert into bgt_assetmethodstep(id, name) values (4, 'เบิกจ่ายเงิน');
insert into bgt_assetmethodstep(id, name) values (5, 'ประกาศสอบราคา');
insert into bgt_assetmethodstep(id, name) values (6, 'ยื่นซองสอบราคา');
insert into bgt_assetmethodstep(id, name) values (7, 'เปิดซองสอบราคา');
insert into bgt_assetmethodstep(id, name) values (8, 'ขออนุมัติผลเปิดซองรับราคา');
insert into bgt_assetmethodstep(id, name) values (9, 'ประกาศประกวดราคา');
insert into bgt_assetmethodstep(id, name) values (10, 'ยื่นซองประกวดราคา');
insert into bgt_assetmethodstep(id, name) values (11, 'เปิดซองประกวดราคา');
insert into bgt_assetmethodstep(id, name) values (12, 'ประกาศร่าง Tor ครั้งที่ 1');
insert into bgt_assetmethodstep(id, name) values (13, 'ประกาศร่าง Tor ครั้งที่ 2');
insert into bgt_assetmethodstep(id, name) values (14, 'ประกาศผลการพิจารณาผู้มีสิทธิ์ประกวดราคา');
insert into bgt_assetmethodstep(id, name) values (15, 'ประมูล ณ ตลาดกลาง');
insert into bgt_assetmethodstep(id, name) values (16, 'ประกาศผู้ชนะการเสนอราคา');

insert into bgt_assetmethodstep_join(bgt_assetmethod_id,bgt_assetmethodstep_id,steporder) values(1,1,0);
insert into bgt_assetmethodstep_join(bgt_assetmethod_id,bgt_assetmethodstep_id,steporder) values(1,2,1);
insert into bgt_assetmethodstep_join(bgt_assetmethod_id,bgt_assetmethodstep_id,steporder) values(1,3,2);
insert into bgt_assetmethodstep_join(bgt_assetmethod_id,bgt_assetmethodstep_id,steporder) values(1,4,3);

insert into bgt_assetmethodstep_join(bgt_assetmethod_id,bgt_assetmethodstep_id,steporder) values(2,5,0);
insert into bgt_assetmethodstep_join(bgt_assetmethod_id,bgt_assetmethodstep_id,steporder) values(2,6,1);
insert into bgt_assetmethodstep_join(bgt_assetmethod_id,bgt_assetmethodstep_id,steporder) values(2,7,2);
insert into bgt_assetmethodstep_join(bgt_assetmethod_id,bgt_assetmethodstep_id,steporder) values(2,8,3);
insert into bgt_assetmethodstep_join(bgt_assetmethod_id,bgt_assetmethodstep_id,steporder) values(2,1,4);
insert into bgt_assetmethodstep_join(bgt_assetmethod_id,bgt_assetmethodstep_id,steporder) values(2,2,5);
insert into bgt_assetmethodstep_join(bgt_assetmethod_id,bgt_assetmethodstep_id,steporder) values(2,3,6);
insert into bgt_assetmethodstep_join(bgt_assetmethod_id,bgt_assetmethodstep_id,steporder) values(2,4,7);

insert into bgt_assetmethodstep_join(bgt_assetmethod_id,bgt_assetmethodstep_id,steporder) values(3,9,0);
insert into bgt_assetmethodstep_join(bgt_assetmethod_id,bgt_assetmethodstep_id,steporder) values(3,10,1);
insert into bgt_assetmethodstep_join(bgt_assetmethod_id,bgt_assetmethodstep_id,steporder) values(3,11,2);
insert into bgt_assetmethodstep_join(bgt_assetmethod_id,bgt_assetmethodstep_id,steporder) values(3,8,3);
insert into bgt_assetmethodstep_join(bgt_assetmethod_id,bgt_assetmethodstep_id,steporder) values(3,1,4);
insert into bgt_assetmethodstep_join(bgt_assetmethod_id,bgt_assetmethodstep_id,steporder) values(3,2,5);
insert into bgt_assetmethodstep_join(bgt_assetmethod_id,bgt_assetmethodstep_id,steporder) values(3,3,6);
insert into bgt_assetmethodstep_join(bgt_assetmethod_id,bgt_assetmethodstep_id,steporder) values(3,4,7);

insert into bgt_assetmethodstep_join(bgt_assetmethod_id,bgt_assetmethodstep_id,steporder) values(4,1,0);
insert into bgt_assetmethodstep_join(bgt_assetmethod_id,bgt_assetmethodstep_id,steporder) values(4,2,1);
insert into bgt_assetmethodstep_join(bgt_assetmethod_id,bgt_assetmethodstep_id,steporder) values(4,3,2);
insert into bgt_assetmethodstep_join(bgt_assetmethod_id,bgt_assetmethodstep_id,steporder) values(4,4,3);

insert into bgt_assetmethodstep_join(bgt_assetmethod_id,bgt_assetmethodstep_id,steporder) values(5,1,0);
insert into bgt_assetmethodstep_join(bgt_assetmethod_id,bgt_assetmethodstep_id,steporder) values(5,2,1);
insert into bgt_assetmethodstep_join(bgt_assetmethod_id,bgt_assetmethodstep_id,steporder) values(5,3,2);
insert into bgt_assetmethodstep_join(bgt_assetmethod_id,bgt_assetmethodstep_id,steporder) values(5,4,3);

insert into bgt_assetmethodstep_join(bgt_assetmethod_id,bgt_assetmethodstep_id,steporder) values(6,12,0);
insert into bgt_assetmethodstep_join(bgt_assetmethod_id,bgt_assetmethodstep_id,steporder) values(6,13,1);
insert into bgt_assetmethodstep_join(bgt_assetmethod_id,bgt_assetmethodstep_id,steporder) values(6,10,2);
insert into bgt_assetmethodstep_join(bgt_assetmethod_id,bgt_assetmethodstep_id,steporder) values(6,14,3);
insert into bgt_assetmethodstep_join(bgt_assetmethod_id,bgt_assetmethodstep_id,steporder) values(6,15,4);
insert into bgt_assetmethodstep_join(bgt_assetmethod_id,bgt_assetmethodstep_id,steporder) values(6,16,5);
insert into bgt_assetmethodstep_join(bgt_assetmethod_id,bgt_assetmethodstep_id,steporder) values(6,1,6);
insert into bgt_assetmethodstep_join(bgt_assetmethod_id,bgt_assetmethodstep_id,steporder) values(6,2,7);
insert into bgt_assetmethodstep_join(bgt_assetmethod_id,bgt_assetmethodstep_id,steporder) values(6,3,8);
insert into bgt_assetmethodstep_join(bgt_assetmethod_id,bgt_assetmethodstep_id,steporder) values(6,4,9);
    
-- version 16
-- Modifed Date : May 28, 2013
	update app_info set db_version=16;
	alter table pln_activity add  (activityLevel number(4));
	
-- version 17 
-- Modified Date : July 7, 2013
-- change to number(19,2)
	update app_info set db_version=17;
	
	alter table bgt_budgetproposal add (amountallocatedtemp number(19,2) );
	update bgt_budgetproposal set amountallocatedtemp = amountallocated;
	update bgt_budgetproposal set  amountallocated = null;
	
	alter table bgt_budgetproposal modify ( amountallocated number(19,2));
	update bgt_budgetproposal set amountallocated = amountallocatedtemp;
	alter table bgt_budgetproposal drop (amountallocatedtemp);
	


	alter table bgt_allocationrecord add ( amountallocatedtemp number(19,2));
	update bgt_allocationrecord set amountallocatedtemp=amountallocated;
	update bgt_allocationrecord set amountallocated=null;

	alter table bgt_allocationrecord modify ( amountallocated number(19,2) );
	update bgt_allocationrecord set amountallocated=amountallocatedtemp;
	alter table bgt_allocationrecord drop (amountallocatedtemp);

    alter table pln_monthlyBgtReport add (budgetplan2 number(19,2), budgetresult2 number(19,2) );
	update pln_monthlyBgtReport set budgetplan2 = budgetplan, budgetresult2 = budgetresult;
	update pln_monthlyBgtReport set  budgetplan = null, budgetresult=null;
	
	alter table pln_monthlyBgtReport modify ( budgetplan number(19,2),  budgetresult number(19,2));
	update pln_monthlyBgtReport set budgetplan = budgetplan2, budgetresult = budgetresult2;
	alter table pln_monthlyBgtReport drop (budgetplan2,budgetresult2);
  
-- version 18
-- Modified Date : July 9, 2013
	update app_info set db_version=18;
	
	alter table PLN_ACTIVITYTARGET add (targetvaluetemp number(19,2), budgetallocatedtemp number(19,2) );
	update PLN_ACTIVITYTARGET set targetvaluetemp = targetvalue, budgetallocatedtemp=budgetallocated;
	update PLN_ACTIVITYTARGET set  targetvalue = null,budgetallocated=null;
	
	alter table PLN_ACTIVITYTARGET modify ( targetvalue number(19,2), budgetallocated number(19,2));
	update PLN_ACTIVITYTARGET set targetvalue = targetvaluetemp, budgetallocated=budgetallocatedtemp;
	alter table PLN_ACTIVITYTARGET drop (targetvaluetemp,budgetallocatedtemp);
	
	
	alter table PLN_ACTIVITYTARGETREPORT add (targetvaluetemp number(19,2) );
	update PLN_ACTIVITYTARGETREPORT set targetvaluetemp = targetvalue;
	update PLN_ACTIVITYTARGETREPORT set  targetvalue = null;
	
	alter table PLN_ACTIVITYTARGETREPORT modify ( targetvalue number(19,2));
	update PLN_ACTIVITYTARGETREPORT set targetvalue = targetvaluetemp;
	alter table PLN_ACTIVITYTARGETREPORT drop (targetvaluetemp);
	
	alter table PLN_MONTHLYACTREPORT add (activityplantemp number(19,2), activityresulttemp number(19,2) );
	update PLN_MONTHLYACTREPORT set activityplantemp = activityplan, activityresulttemp=activityresult;
	update PLN_MONTHLYACTREPORT set  activityplan = null,activityresult=null;
	
	alter table PLN_MONTHLYACTREPORT modify ( activityplan number(19,2), activityresulttemp number(19,2));
	update PLN_MONTHLYACTREPORT set activityplan = activityplantemp, activityresult=activityresulttemp;
	alter table PLN_MONTHLYACTREPORT drop (activityplantemp,activityresulttemp);
	
	alter table PLN_ACTIVITYTARGETRESULT add (resulttemp number(19,2) );
	update PLN_ACTIVITYTARGETRESULT set resulttemp = result;
	update PLN_ACTIVITYTARGETRESULT set  result = null;
	
	alter table PLN_ACTIVITYTARGETRESULT modify ( result number(19,2));
	update PLN_ACTIVITYTARGETRESULT set result = resulttemp;
	alter table PLN_ACTIVITYTARGETRESULT drop (resulttemp);
	
	
-- version 19
-- Modified Date: July 24, 2013
	update app_info set db_version=19;
	
    create table BGT_ASSETBUDGETPLAN (
        id number(19,0) not null,
        actualAmount double precision,
        actualDate timestamp,
        budgetOrder number(10,0),
        planAmount double precision,
        planDate timestamp,
        BGT_ASSETALLOCATION_ID number(19,0),
        primary key (id)
    );
    
    alter table BGT_ASSETBUDGETPLAN 
        add constraint FKF91CC32E2A6DA39 
        foreign key (BGT_ASSETALLOCATION_ID) 
        references BGT_ASSETALLOCATION;
        
    create sequence BGT_ASSETBUDGETPLAN_SEQ; 	
    
-- version 20
-- Modifed Date: August 2, 2013 
    update app_info set db_version=20;
    
    alter table bgt_assetbudgetplan add (planinstallmentdate timestamp(6), actualinstallmentdate timestamp(6), remark varchar2(255));
	alter table bgt_assetallocation add (contractedBudgetActual number(19,2), contractedBudgetPlan number(19,2));
	
	update bgt_assetmethodstep set name ='กำหนดคุณลักษณะ' where id=12;
	update bgt_assetmethodstep set name ='ประกาศร่าง TOR ' where id=13;
	update bgt_assetmethodstep set name ='ประกาศผลการพิจารณาเบื้องต้น' where id=14;
	insert into bgt_assetmethodstep  (id, name) values (17, 'เสนอรับราคา');
	insert into bgt_assetmethodstep  (id, name) values (18, 'ระยะเวลาดำเนินการ');
	
	delete bgt_assetmethodstep_join where bgt_assetmethod_id=2 and bgt_assetmethodstep_id=6 and steporder=1;
	delete bgt_assetmethodstep_join where bgt_assetmethod_id=2 and bgt_assetmethodstep_id=7 and steporder=2;
	delete bgt_assetmethodstep_join where bgt_assetmethod_id=2 and bgt_assetmethodstep_id=8 and steporder=3;
	update bgt_assetmethodstep_join set steporder = steporder - 3 where bgt_assetmethod_id=2 and steporder>0;
	
	update bgt_assetmethodstep_join set steporder = steporder +1 where bgt_assetmethod_id=6 and steporder>=5;
	insert into bgt_assetmethodstep_join (bgt_assetmethod_id, bgt_assetmethodstep_id, steporder) values (6, 17, 5);
	delete bgt_assetmethodstep_join where bgt_assetmethod_id=6 and steporder >=8;
	insert into bgt_assetmethodstep_join (bgt_assetmethod_id, bgt_assetmethodstep_id, steporder) values (6, 18, 8);
    
	
-- version 21
-- Modified Date: August 8, 2013
update app_info set db_version=21;
insert into bgt_assetmethod values (7, 'จ้างที่ปรึกษาวิธีตกลง');
insert into bgt_assetmethod values (8, 'จ้างที่ปรึกษาวิธีคัดเลือก');
insert into bgt_assetmethod values (9, 'จ้างออกแบบวิธีตกลง');
insert into bgt_assetmethod values (10, 'จ้างออกแบบวิธีคัดเลือก');
insert into bgt_assetmethod values (11, 'จ้างออกแบบวิธีคัดเลือกแบบจำกัดข้อกำหนด');
insert into bgt_assetmethod values (12, 'จ้างออกแบบวิธีพิเศษ');

insert into bgt_assetmethodstep_join values (7, 1, 0);
insert into bgt_assetmethodstep_join values (7, 2, 1);
insert into bgt_assetmethodstep_join values (7, 3, 2);
insert into bgt_assetmethodstep_join values (7, 4, 3);

insert into bgt_assetmethodstep_join values (8, 1, 0);
insert into bgt_assetmethodstep_join values (8, 2, 1);
insert into bgt_assetmethodstep_join values (8, 3, 2);
insert into bgt_assetmethodstep_join values (8, 4, 3);

insert into bgt_assetmethodstep_join values (9, 1, 0);
insert into bgt_assetmethodstep_join values (9, 2, 1);
insert into bgt_assetmethodstep_join values (9, 3, 2);
insert into bgt_assetmethodstep_join values (9, 4, 3);

insert into bgt_assetmethodstep_join values (10, 1, 0);
insert into bgt_assetmethodstep_join values (10, 2, 1);
insert into bgt_assetmethodstep_join values (10, 3, 2);
insert into bgt_assetmethodstep_join values (10, 4, 3);

insert into bgt_assetmethodstep_join values (11, 1, 0);
insert into bgt_assetmethodstep_join values (11, 2, 1);
insert into bgt_assetmethodstep_join values (11, 3, 2);
insert into bgt_assetmethodstep_join values (11, 4, 3);

insert into bgt_assetmethodstep_join values (12, 1, 0);
insert into bgt_assetmethodstep_join values (12, 2, 1);
insert into bgt_assetmethodstep_join values (12, 3, 2);
insert into bgt_assetmethodstep_join values (12, 4, 3);

-- version 22
-- Modified Date: August 18, 2013
update app_info set db_version=22;
    
	create table BGT_ASSETCATEGORY (
        id number(19,0) not null,
        name varchar2(255),
        code varchar2(255),
        primary key (id)
    );
    
    alter table BGT_ASSETBUDGET add (
    	ASSETCATEGORY_ID number(19,0)
    );
    
    alter table BGT_ASSETBUDGET     				   
        add constraint FK32D3493329384AB2 
        foreign key (ASSETCATEGORY_ID) 
        references BGT_ASSETCATEGORY;

	create sequence BGT_ASSETCATEGORY_SEQ;
	
-- version 23
-- Modified Date: August 23, 2013
update app_info set db_version=23;

update bgt_assetmethodstep set name='สนองรับราคา' where id=17;
update bgt_assetmethodstep set name='แจ้งผล' where id=16;
update bgt_assetmethodstep_join set steporder = steporder+1 where bgt_assetmethod_id=6 and steporder>1;
INSERT INTO bgt_assetmethodstep_join VALUES (6,9,2);


alter table bgt_assetallocation add (estimatedcost number(19,2));
 
-- version 24
-- Modified Date: December 15, 2013
update app_info set db_version=24;
alter table bgt_assetallocation add (bgt_assetCategory_id number(19,2));
alter table BGT_ASSETALLOCATION	               
	add constraint FK389D89EF83923378
    foreign key (bgt_ASSETCATEGORY_ID) 
    references BGT_ASSETCATEGORY;

create sequence BGT_BUDGETPROPOSALROUND_SEQ;
create table BGT_BUDGETPROPOSALROUND (
        id number(19,0) not null,
        fiscalYear number(10,0),
        name varchar2(255 char),
        officialDate date,
        roundNo number(10,0),
        primary key (id)
    );
    
alter table BGT_BUDGETPROPOSAL add (BGT_BUDGETPROPOSALROUND_ID number(19,0));
    
alter table BGT_BUDGETPROPOSAL 
        add constraint FK_fcfkwtx7il9le71yjxuyhk0gl 
        foreign key (BGT_BUDGETPROPOSALROUND_ID) 
        references BGT_BUDGETPROPOSALROUND;
