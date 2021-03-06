package cn.superid.webapp.controller;

import cn.superid.utils.StringUtil;
import cn.superid.webapp.annotation.RequiredPermissions;
import cn.superid.webapp.controller.VO.CreateAllianceResultVO;
import cn.superid.webapp.enums.SuperIdTitle;
import cn.superid.webapp.enums.type.PublicType;
import cn.superid.webapp.forms.AllianceCertificationForm;
import cn.superid.webapp.forms.AllianceCreateForm;
import cn.superid.webapp.forms.CreateAffairForm;
import cn.superid.webapp.forms.SimpleResponse;
import cn.superid.webapp.model.AllianceEntity;
import cn.superid.webapp.security.AlliancePermissions;
import cn.superid.webapp.security.GlobalValue;
import cn.superid.webapp.service.IAffairMemberService;
import cn.superid.webapp.service.IAffairService;
import cn.superid.webapp.service.IAllianceService;
import cn.superid.webapp.service.IUserService;

import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by xiaofengxu on 16/9/8.
 */
@Controller
@RequestMapping("/alliance")
public class AllianceController {

    @Autowired
    private IAllianceService allianceService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IAffairMemberService affairMemberService;

    @Autowired
    private IAffairService affairService;

    @ApiOperation(value = "创建盟", response = AllianceEntity.class, notes = "code为盟代码,affairs选择的事务,逗号分开")
    @RequestMapping(value = "/create_alliance", method = RequestMethod.POST)
    public SimpleResponse createAlliance(String name,String affairs,String code) {

        if (StringUtil.isEmpty(name)) {
            return SimpleResponse.error("name cannot be null");
        }
        AllianceCreateForm allianceCreateForm = new AllianceCreateForm();
        allianceCreateForm.setUserId(userService.currentUserId());
        allianceCreateForm.setIsPersonal(false);
        allianceCreateForm.setName(name);
        allianceCreateForm.setCode(code);
        allianceCreateForm.setRoleTitle(SuperIdTitle.ALLIANCE_OWNER_NAME);
        AllianceEntity allianceEntity = allianceService.createAlliance(allianceCreateForm);

        if (allianceEntity == null) {
            return SimpleResponse.error("create_alliance_error");
        }

        String[] affairList = affairs.split(",");
        int index = 0;
        for (String affairName : affairList) {//在根事务下面建立选择的事务
            if(affairName.equals("")){
                break;
            }
            CreateAffairForm createAffairForm = new CreateAffairForm();
            createAffairForm.setName(affairName);
            createAffairForm.setParentAffairId(allianceEntity.getRootAffairId());
            createAffairForm.setNumber(index++);
            createAffairForm.setAllianceId(allianceEntity.getId());
            createAffairForm.setOperationRoleId(allianceEntity.getOwnerRoleId());
            createAffairForm.setPublicType(PublicType.TO_ALLIANCE);
            try {
                affairService.createAffair(createAffairForm);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        CreateAllianceResultVO createAllianceResultVO = new CreateAllianceResultVO();
        allianceEntity.copyPropertiesTo(createAllianceResultVO);
        createAllianceResultVO.setAffairTree(affairService.getAffairTree(allianceEntity.getId()));
        return SimpleResponse.ok(createAllianceResultVO);
    }

    @ApiOperation(value = "验证盟代码不重复", response =Boolean.class)
    @RequestMapping(value = "/valid_code", method = RequestMethod.POST)
    public SimpleResponse validCode(String code){
        return new SimpleResponse(allianceService.validName(code));
    }


    @ApiOperation(value = "填写盟真实信息", response =Long.class)
    @RequiredPermissions(alliance = AlliancePermissions.EditAllianceInfo)
    @RequestMapping(value = "/add_certification", method = RequestMethod.POST)
    public SimpleResponse addCertification(@RequestBody AllianceCertificationForm allianceCertificationForm,long roleId){
        if(allianceService.addAllianceCertification(allianceCertificationForm,roleId, GlobalValue.currentAllianceId())){
            return SimpleResponse.ok("提交成功,请等待验证");
        }
        return SimpleResponse.error("提交失败,您已提交过申请");
    }


    @ApiOperation(value = "修改盟真实信息", response =Long.class)
    @RequiredPermissions(alliance = AlliancePermissions.EditAllianceInfo)
    @RequestMapping(value = "/edit_certification", method = RequestMethod.POST)
    public SimpleResponse editCertification(@RequestBody AllianceCertificationForm allianceCertificationForm, long roleId){
        return SimpleResponse.ok(allianceService.editAllianceCertification(allianceCertificationForm,roleId));
    }

    @ApiOperation(value = "获取一个用户在一个盟里的默认角色,返回值为roleId", response =Long.class)
    @RequestMapping(value = "/default_role_in_alliance",method = RequestMethod.POST)
    public SimpleResponse getDefaultRoleIdInAlliance(long allianceId){
        return SimpleResponse.ok(allianceService.getDefaultRoleIdFromAlliance(allianceId));
    }

    @ApiOperation(value = "校验盟名称是否重复", response =Long.class)
    @RequestMapping(value = "/verify_alliance_name",method = RequestMethod.GET)
    public SimpleResponse verifyAllianceName(String name){
        return SimpleResponse.ok(allianceService.verifyAllianceName(name));
    }

    @ApiOperation(value = "获取一个用户底下所有盟信息", response =Long.class)
    @RequestMapping(value = "/alliance_list",method = RequestMethod.GET)
    public SimpleResponse getAllianceList(){
        return SimpleResponse.ok(allianceService.getAllianceList());
    }

    @ApiOperation(value = "获取一个盟里所有角色", response =Long.class)
    @RequestMapping(value = "/get_role",method = RequestMethod.GET)
    public SimpleResponse getAllRoleByAlliance(Long allianceId){
        if (allianceId == null) {
            return SimpleResponse.error("参数不能为空");
        }
        return SimpleResponse.ok(allianceService.getRoleByAlliance(allianceId));
    }

}
