package cn.superid.webapp.enums;

import com.wordnik.swagger.annotations.Api;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Created by xiaofengxu on 16/9/5.
 */
@Api(value = "性别类型")
public class GenderType {
    private static final int secret=0;
    private static final int man =1;
    private static final int woman=2;
}
