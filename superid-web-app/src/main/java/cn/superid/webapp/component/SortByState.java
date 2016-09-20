package cn.superid.webapp.component;

import cn.superid.webapp.controller.forms.OwnContractResult;

import java.util.Comparator;

/**
 * Created by njuTms on 16/8/23.
 */
public class SortByState implements Comparator {
    public int compare(Object o1, Object o2) {
        OwnContractResult s1 = (OwnContractResult) o1;
        OwnContractResult s2 = (OwnContractResult) o2;
        if (s1.getState() == 0)
            return 0;
        return s1.getState()-s2.getState();
    }
}
