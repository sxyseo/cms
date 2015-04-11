package com.calm.cms.api.compile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import javassist.CannotCompileException;
import javassist.CtMember;
import javassist.NotFoundException;

import com.alibaba.fastjson.annotation.JSONField;
import com.calm.cms.api.entity.BaseColumnData;
import com.calm.cms.api.entity.Relation;
import com.calm.cms.api.entity.TableColumn;
import com.calm.cms.api.entity.TableDefined;
import com.calm.cms.api.processor.FieldProcessor;
import com.calm.framework.util.StringUtil;
import com.calm.javassist.helper.AnnationHelper;
import com.calm.javassist.helper.ClassHelper;
import com.calm.javassist.helper.FieldHelper;
import com.calm.javassist.helper.MemberHelper;
import com.calm.javassist.helper.MethodHelper;

public class Compiler {
	private static final String CLASS_NAME = BaseColumnData.class.getName();
	private static final Map<String,Class<? extends BaseColumnData>> classMap = new HashMap<String, Class<? extends BaseColumnData>>();
	public static Class<? extends BaseColumnData> getClass(TableDefined tableDef,ClassLoader classLoader,ApplicationContext context) throws ClassNotFoundException{
		
		String className = CLASS_NAME+tableDef.getId();
		Class<? extends BaseColumnData> clazz = classMap.get(className);
		if(clazz!=null){
			return clazz;
		}
		
		try {
			ClassHelper helper = ClassHelper.getHelper(classLoader);
			helper.makeClass(className).setSupperClass(CLASS_NAME);
			processFieldAndMethod(helper,tableDef,context);
			clazz = helper.toClass();
			classMap.put(className, clazz);
			return clazz;
		} catch (NotFoundException | CannotCompileException e) {
			throw new ClassNotFoundException(e.getMessage());
		}
	}

	private static void processFieldAndMethod(ClassHelper helper,
			TableDefined tableDef, ApplicationContext context) throws CannotCompileException {
		List<TableColumn> columns = tableDef.getColumns();
		for(TableColumn tc:columns){
			String id = tc.getId().getId();
			Relation relation = tc.getRelation();
			String name;
			if(Relation.ONE2MANY==relation){
				name = List.class.getName();
			}else{
				FieldProcessor bean = context.getBean(tc.getProcessor().getProcessId(), FieldProcessor.class);
				name = bean.getType().getName();
			}
			
			FieldHelper field = helper.addField("private "+name+" "+id+";");
			String methodName=StringUtil.upperFrist(id);
			
			MethodHelper methodHelper = helper.addGetMethod("get"+methodName, field);
			
			AnnationHelper<MemberHelper<? extends CtMember>> annationHelper = methodHelper.addAnnation(JSONField.class);
			annationHelper.addStringMember("name", id);
			
			if(Relation.ONE2MANY==relation){
				annationHelper.addBooleanMember("serialize", false);
			}
			annationHelper.end();
			if(Relation.MANY2ONE==relation){
				StringBuffer methodStr= new StringBuffer();
				methodStr.append("public Object get");
				methodStr.append(methodName);
				methodStr.append("_ID(){\n");
				methodStr.append("\tif(null==");
				methodStr.append(id);
				methodStr.append("){\n");
				methodStr.append("\t\treturn null;\n");
				methodStr.append("\t}\n");
				methodStr.append("\treturn ");
				methodStr.append(id);
				methodStr.append(".getId_();\n}");
				MethodHelper addMethod = helper.addMethod(methodStr.toString());
				AnnationHelper<MemberHelper<? extends CtMember>> addAnnation = addMethod.addAnnation(JSONField.class);
				addAnnation.addStringMember("name", id+"_ID").end();
			}
			helper.addSetMethod("set"+methodName, field);
		}
	}
}
