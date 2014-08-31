Ext.define('com.calm.cms.module.TableColumn', {
    extend: 'Ext.data.Model',
    fields: [{name:'tableId'},
             {name:'name'},
             {name:'columnName'}, 
             {name:'defaultValue'}, 
             {name:'relation'}, 
             {name:'relationName'}, 
             {name:'processorId'}, 
             {name:'processorName'},
             {name:'required'}]
});