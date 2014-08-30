Ext.define('com.calm.cms.module.TableColumn', {
    extend: 'Ext.data.Model',
    fields: [{name:'columnId'},{name:'columnName'}, {name:'defaultValue'}, {name:'relation'}, {name:'relationColumn'}, {name:'required'}]
});