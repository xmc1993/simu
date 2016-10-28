package cn.superid.admin.controller;

import cn.superid.admin.form.CertificationForm;
import cn.superid.admin.form.SimpleResponse;
import cn.superid.admin.service.IAllianceCertificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Created by njuTms on 16/10/9.
 */
@Controller
@RequestMapping("/alliance_certification")
public class AllianceCertificationController {
    @Autowired
    private IAllianceCertificationService allianceCertificationService;

    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public SimpleResponse getAllianceCertificationList(){
        return SimpleResponse.ok(allianceCertificationService.showUncheckedCertification());
    }

    @RequestMapping(value = "/info",method = RequestMethod.POST)
    public SimpleResponse getCertificationInfo(@RequestBody CertificationForm certificationForm){
        return SimpleResponse.ok(allianceCertificationService.showCertificationInfo(certificationForm.getId(),certificationForm.getAllianceId()));
    }

    @RequestMapping(value = "/agree",method = RequestMethod.POST)
    public SimpleResponse agreeCertification(@RequestBody CertificationForm certificationForm){
        return SimpleResponse.ok(allianceCertificationService.agreeCertification(certificationForm.getId(),certificationForm.getAllianceId(),certificationForm.getCheckReason(),certificationForm.getUserName()));
    }

    @RequestMapping(value = "/reject",method = RequestMethod.POST)
    public SimpleResponse rejectCertification(@RequestBody CertificationForm certificationForm){
        return SimpleResponse.ok(allianceCertificationService.rejectCertification(certificationForm.getId(),certificationForm.getAllianceId(),certificationForm.getCheckReason(),certificationForm.getUserName()));
    }
}
