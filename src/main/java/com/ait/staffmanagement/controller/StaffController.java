package com.ait.staffmanagement.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ait.staffmanagement.model.Office;
import com.ait.staffmanagement.model.Skill;
import com.ait.staffmanagement.model.Staff;
import com.ait.staffmanagement.model.DTO.CreateDTO;
import com.ait.staffmanagement.service.office.IOfficeService;
import com.ait.staffmanagement.service.skill.ISkillService;
import com.ait.staffmanagement.service.staff.IStaffService;

@Controller
@RequestMapping("/staff")
public class StaffController {

    @Autowired
    private IOfficeService officeService;

    @Autowired
    private ISkillService skillService;
    
    @Autowired
    private IStaffService staffService;

   
    
    @GetMapping
    public ModelAndView listStaff(){
       ModelAndView modelAndView = new ModelAndView();
       modelAndView.setViewName("staff/list");
//       modelAndView.addObject("staffs",staffService.findAll());
       modelAndView.addObject("staffs", staffService.findAllByDeleted(true));
       return modelAndView;
   }
    
    @GetMapping("/info/{id}")
    public ModelAndView inforStaff(@PathVariable Long id) {
    	ModelAndView modelAndView = new ModelAndView("staff/info");
    	Optional<Staff> ifStaff = staffService.findById(id);
    	
    	modelAndView.addObject("staffInfo", ifStaff);
    	
    	return modelAndView;
    }
    

    @GetMapping("/create")
    public ModelAndView getFormCreate() {
        ModelAndView modelAndView = new ModelAndView("staff/create");
        Iterable<Office> offices = officeService.findAll();
        Iterable<Skill> skills = skillService.findAll();

        modelAndView.addObject("createStaff", new CreateDTO());
        modelAndView.addObject("offices", offices);
        modelAndView.addObject("skills", skills);
        return modelAndView;
    }

   
    @PostMapping(value = "/create",  produces = "application/json;charset=UTF-8")
    public ModelAndView createStaff(@Valid @ModelAttribute("createStaff") CreateDTO createDTO,
            BindingResult bindingResult,RedirectAttributes redirectAttributes) throws ParseException {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasFieldErrors()){
            redirectAttributes.addFlashAttribute("mess", "Something is wrong !");
            modelAndView.setViewName("redirect:/staff");
            return modelAndView;
        }
        Optional<Staff> staffOptional = staffService.findByEmail(createDTO.getEmail());

        if (staffOptional.isPresent()) {
            modelAndView.setViewName("staff/create");
            modelAndView.addObject("offices", officeService.findAll());
            modelAndView.addObject("skills", skillService.findAll());
            modelAndView.addObject("mess", "Email already exists");

        } else {
            Date dob = new SimpleDateFormat("yyyy-dd-MM").parse(createDTO.getDateOfBirth());
            Date ecd = new SimpleDateFormat("yyyy-dd-MM").parse(createDTO.getEntryCompanyDate());
            List<Skill> skillList = new ArrayList<>();
            for (Long item : createDTO.getSkills()){
                skillList.add(skillService.findById(item).get());
            }
            Staff staff = new Staff(createDTO.getFullName(), createDTO.getEmail(), createDTO.getAddress(), dob,
                    ecd, createDTO.getIntroduce(), officeService.findById(createDTO.getOffice()).get(),skillList);
            staffService.save(staff);
            redirectAttributes.addFlashAttribute("mess", "Success !");
            modelAndView.setViewName("redirect:/staff");
        }
        return modelAndView;
    }

    @GetMapping("/edit-staff/{id}")
    public ModelAndView getFormCreate(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        Optional<Staff> staffOptional = staffService.findById(id);
        if (staffOptional.isPresent()){
            if(!staffOptional.get().getDeleted()){
                redirectAttributes.addFlashAttribute("mess", "This staff is Locked");
                modelAndView.setViewName("redirect:/staff");
            }else{
                modelAndView.setViewName("staff/edit");
                modelAndView.addObject("createStaff", staffOptional.get().toCreateDTO());
                modelAndView.addObject("offices", officeService.findAll());
                modelAndView.addObject("skills", skillService.findAll());
            }
        }
        else{
            redirectAttributes.addFlashAttribute("mess", "Staff is not exist");
            modelAndView.setViewName("redirect:/staff");
        }
        return modelAndView;
    }

    @PostMapping(value = "/edit-staff/{id}",  produces = "application/json;charset=UTF-8")
    public ModelAndView editStaff(@PathVariable Long id, @Valid @ModelAttribute("createStaff") CreateDTO createDTO,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes) throws ParseException {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasFieldErrors()){
            redirectAttributes.addFlashAttribute("mess", "Something is wrong !");
            modelAndView.setViewName("redirect:/staff");
            return modelAndView;
        }
        Optional<Staff> staffOptionalbyId = staffService.findById(id);
        if (staffOptionalbyId.isPresent()){
            Optional<Staff> staffOptionalByEmail = staffService.findByEmail(staffOptionalbyId.get().getEmail());
            if (staffOptionalByEmail.isPresent() && staffOptionalByEmail.get().getId() !=id) {
                modelAndView.setViewName("staff/edit");
                modelAndView.addObject("offices", officeService.findAll());
                modelAndView.addObject("skills", skillService.findAll());
                modelAndView.addObject("mess", "Email already exists");
            } else {
                Date dob = new SimpleDateFormat("yyyy-dd-MM").parse(createDTO.getDateOfBirth());
                Date ecd = new SimpleDateFormat("yyyy-dd-MM").parse(createDTO.getEntryCompanyDate());
                List<Skill> skillList = new ArrayList<>();
                for (Long item : createDTO.getSkills()) {
                    skillList.add(skillService.findById(item).get());
                }
                Staff staff = new Staff(createDTO.getId(), createDTO.getFullName(), createDTO.getEmail(), createDTO.getAddress(), dob,
                        ecd, createDTO.getIntroduce(), officeService.findById(createDTO.getOffice()).get(), skillList);
                staffService.save(staff);
                redirectAttributes.addFlashAttribute("mess", "Success !");
                modelAndView.setViewName("redirect:/staff");
            }
        }else{
            redirectAttributes.addFlashAttribute("mess", "Staff is not exist");
            modelAndView.setViewName("redirect:/staff");
        }
        return modelAndView;
    }
}
