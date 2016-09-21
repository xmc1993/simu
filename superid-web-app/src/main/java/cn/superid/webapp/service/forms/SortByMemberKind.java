package cn.superid.webapp.service.forms;

import java.util.Comparator;

/**
 * Created by njuTms on 16/8/26.
 */
public class SortByMemberKind implements Comparator {
    public int compare(Object o1, Object o2) {
        ContractMemberResult s1 = (ContractMemberResult) o1;
        ContractMemberResult s2 = (ContractMemberResult) o2;
        if (s1.getKind() == 1)
            return -1;
        return s1.getKind()-s2.getKind();
    }
}
