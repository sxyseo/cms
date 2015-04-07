/*!
 * Ext JS Library 4.0
 * Copyright(c) 2006-2011 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */

Ext.define('com.calm.cms.ui.DataManager', {
    extend: 'com.calm.module.Module',

    requires: [
        'Ext.tree.Panel',
        'Ext.form.field.Text',
        'com.calm.cms.module.TableDefined'
    ],

    id: 'cms-data-manager',
    
    init : function(){
        this.launcher = {
            text: '数据管理',
            iconCls:'cms-data-manager-icon'
        };
    },
    /**
     * @cfg {String} tableItemSelector
     * This property is passed to the DataView for the desktop to select table items.
     * If the {@link #tableTpl} is modified, this will probably need to be modified as
     * well.
     */
    tableItemSelector: 'div.cms-ux-table',
    /**
     * @cfg {String} tableTpl
     * This XTemplate is used to render items in the DataView. If this is changed, the
     * {@link tableItemSelect} will probably also need to changed.
     */
    tableTpl: [
        '<tpl for=".">',
            '<div class="cms-ux-table" id="table-{id}">',
                '<div class="cms-ux-table-icon {iconCls}">',
                    '<img src="',Ext.BLANK_IMAGE_URL,'" title="{name}">',
                '</div>',
                '<span class="cms-ux-table-text">{name}</span>',
            '</div>',
        '</tpl>',
        '<div class="x-clear"></div>'
    ],
    store:Ext.create('Ext.data.Store', {
        model: 'com.calm.cms.module.TableDefined',
        autoLoad: true,
        pageSize: 15000000,
        proxy: {
            type: 'ajax',
            url : 'cms/tableDefined/list',
            reader: {
                type: 'json',
                root: 'list',
                totalProperty: 'total'
            }
        }
    }),
    //------------------------------------------------------
    // Overrideable configuration creation methods

    createDataView: function () {
        var me = this;
        return {
            xtype: 'dataview',
            overItemCls: 'x-view-over',
            trackOver: true,
            itemSelector: me.tableItemSelector,
            store: me.store,
            style: {
                position: 'absolute'
            },
            x: 0, y: 0,
            tpl: new Ext.XTemplate(me.tableTpl)
        };
    },
    createWindow : function(){
    	var me=this;
        var desktop = this.app.getDesktop();
        var win = desktop.getWindow('cms-data-manager-win');
        win = desktop.createWindow({
            id: 'cms-data-manager-win',
            title: '数据管理',
            width:840,
            height:480,
            tools:[{
            	id:'refresh',
            	handler:function(e, target, panel){
            		me.store.reload();
            	}
            }],
            iconCls: 'cms-data-manager-icon',
            animCollapse:false,
            constrainHeader:true,
            layout: 'fit',
            items : [
                me.createDataView()
            ]
        });
        win.tablesView = win.items.getAt(0);
        win.tablesView.on('itemclick', me.onTableItemClick, me);

        return win;
    },
    onTableItemClick: function (dataView, record) {
    	var me = this, module = Ext.create('com.calm.cms.ui.DataTableWindow');
    	module.tableId=record.data.id;
    	module.show();
    },
});
