package com.calm.cms.api.compile;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.BooleanMemberValue;
import javassist.bytecode.annotation.StringMemberValue;

import com.alibaba.fastjson.annotation.JSONField;
import com.calm.cms.api.entity.BaseColumnData;
import com.calm.cms.api.entity.Relation;
import com.calm.cms.api.entity.TableColumn;
import com.calm.cms.api.entity.TableDefined;
import com.calm.framework.util.StringUtil;

public class Compiler {
	private static final String CLASS_NAME="com.calm.cms.entity.proxy.BaseColumnData";
	private static final Map<String,Class<? extends BaseColumnData>> classMap = new HashMap<String, Class<? extends BaseColumnData>>();
	private static Set<ClassLoader> loaders = new HashSet<ClassLoader>();
	public static ClassPool getClassPool(ClassLoader classLoader){
		ClassPool pool = ClassPool.getDefault();
		if(loaders.contains(classLoader)){
			return pool;
		}
		pool.insertClassPath(new LoaderClassPath(classLoader));
		return pool;
	}
	
	@SuppressWarnings("unchecked")
	public static Class<? extends BaseColumnData> getClass(TableDefined tableDef,ClassLoader classLoader) throws ClassNotFoundException{
		CtClass ctClass = null;
		ClassPool classPool = getClassPool(classLoader);
		String className = CLASS_NAME+tableDef.getId();
		Class<? extends BaseColumnData> clazz = classMap.get(className);
		if(clazz!=null){
			return clazz;
		}
		try {
			ctClass = classPool.get(className);
		} catch (NotFoundException e) {
			ctClass = classPool.makeClass(className);
			CtClass supperClass;
			try {
				supperClass = classPool.get("com.calm.cms.api.entity.BaseColumnData");
				ctClass.setSuperclass(supperClass);
				processFieldAndMethod(ctClass,tableDef);
			} catch (NotFoundException | CannotCompileException e1) {
				e1.printStackTrace();
			}
		}
		try {
			clazz = ctClass.toClass(classLoader, classLoader.getClass().getProtectionDomain());
			classMap.put(className, clazz);
			return clazz;
		} catch (CannotCompileException e) {
			throw new ClassNotFoundException(e.getMessage());
		}
	}

	private static void processFieldAndMethod(CtClass ctClass,
			TableDefined tableDef) throws CannotCompileException {
		List<TableColumn> columns = tableDef.getColumns();
		for(TableColumn tc:columns){
			String id = tc.getId().getId();
			CtField f = CtField.make("private Object "+id+";",ctClass);
			ctClass.addField(f);
			String methodName=StringUtil.upperFrist(id);
			Relation relation = tc.getRelation();
			
			CtMethod mthd = CtMethod.make("public Object get"+methodName+"() { return this."+id+"; }", ctClass);
			ConstPool constPool = ctClass.getClassFile().getConstPool();
			Annotation a = new Annotation(JSONField.class.getName(), constPool);
			a.addMemberValue("name",new StringMemberValue(id,constPool));
			if(Relation.ONE2MANY == relation){
				a.addMemberValue("serialize",new BooleanMemberValue(false,constPool));
			}
			AnnotationsAttribute attr = new AnnotationsAttribute(constPool,AnnotationsAttribute.visibleTag);
			attr.setAnnotation(a);
			
			mthd.getMethodInfo().addAttribute(attr);
			ctClass.addMethod(mthd);
			
			mthd = CtNewMethod.make("public void set"+methodName+"(Object val) { this."+id+"=val; }", ctClass);
			
			ctClass.addMethod(mthd);
		}
	}
}
