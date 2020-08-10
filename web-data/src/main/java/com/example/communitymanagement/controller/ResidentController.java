package com.example.communitymanagement.controller;

import com.example.communitymanagement.service.ResidentService;
import com.sun.istack.NotNull;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/resident/data")
@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMIN')")
public class ResidentController {
    @Resource
    ResidentService residentService;

    @RequestMapping("/listAllResident")
    public String listAllAdmin(@NotNull @RequestParam("pageNum") int pageNum, @NotNull @RequestParam("search") String search){
        if(Strings.isBlank(search)){
            return residentService.getResidentList(pageNum,10);
        }
        else{
            return residentService.getResidentListBySearch(pageNum,10,search);
        }
    }

    @RequestMapping("/disableResident")
    public String disableAdmin(@NotNull @RequestParam("residentId") String residentId){
        return residentService.modifyResidentStatus(Long.parseLong(residentId),1);
    }

    @RequestMapping("/enableResident")
    public String enableAdmin(@NotNull @RequestParam("residentId") String residentId){
        return residentService.modifyResidentStatus(Long.parseLong(residentId),0);
    }

    /**
     * TODO 建立完记录表后还需要级联删除记录表中该用户的数据
     * @param residentId 住户id
     * @return success/error
     */
    @RequestMapping("/deleteResident")
    public String deleteResident(@NotNull @RequestParam("residentId") String residentId){
        return residentService.deleteResident(Long.parseLong(residentId));
    }
}
