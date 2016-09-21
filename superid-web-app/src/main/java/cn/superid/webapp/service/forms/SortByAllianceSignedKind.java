package cn.superid.webapp.service.forms;

import cn.superid.webapp.controller.forms.AllianceSigned;

import java.util.Comparator;

/**
 * Created by njuTms on 16/8/26.
 */
public class SortByAllianceSignedKind implements Comparator {
    public int compare(Object o1, Object o2) {
        AllianceSigned s1 = (AllianceSigned) o1;
        AllianceSigned s2 = (AllianceSigned) o2;
        if (s1.getAllianceKind() == 1)
            return -1;
        return s1.getAllianceKind()-s2.getAllianceKind();
    }
}
