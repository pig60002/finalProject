package com.bank.creditCard.application.controller;


import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bank.member.bean.Member;
import com.bank.creditCard.application.model.CardApplicationBean;
import com.bank.creditCard.application.service.CardApplicationService;
import com.bank.creditCard.cardType.model.CardTypeBean;
import com.bank.creditCard.cardType.service.CardTypeService;
import com.bank.creditCard.dto.CardApplicationDTO;
import com.bank.creditCard.dto.MemberDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/card")
public class CardApplicationFrontController {

	private static final String UPLOAD_DIR="uploadsCard";
	
	@Autowired
	private CardApplicationService cService;
	
	@Autowired
	private CardTypeService cTService;
	
	//申請畫面
	@GetMapping("/apply")
	public Map<Integer, String> showApplicationForm(Model m) {
		return cTService.getCardTypeMap();
	}
	
	//檔案存放
	private String saveFile(MultipartFile file,String uploadPath) throws IllegalStateException, IOException {
		if(file.isEmpty()) {
			return null;
		}
		String fileName=UUID.randomUUID().toString()+"_"+file.getOriginalFilename();
		File dest = new File(uploadPath,fileName);
		file.transferTo(dest);
		return "/creditCardImg/"+fileName;
	}
	
	//提交申請資料
	@PostMapping("/submit")
	public ResponseEntity<?> submitApplication(
			@RequestParam("cardType") int cardType,
			@RequestParam("idFront") MultipartFile idFront,
			@RequestParam("idBack") MultipartFile idBack,
			@RequestParam("financialProof") MultipartFile financialProof,
			HttpServletRequest request,
			HttpSession session
			) {
		try {
//			Member member=(Member) session.getAttribute("Member");
//			if (session.getAttribute("Member") == null) {
//			    Member testMember = new Member();
//			    testMember.setMId(1); // 假的使用者 ID
//			    session.setAttribute("Member", testMember);
//			    member=testMember;
//			}
//			if(member==null) {
//				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//			            .body(Map.of("error", "尚未登入"));
//			}
			 Member member = new Member();
			  member.setmId(1);
			String uploadPath=System.getProperty("user.dir") + File.separator + "creditCardImg";
			new File(uploadPath).mkdirs();
			
			String idFrontUrl=saveFile(idFront,uploadPath);
			String idBackUrl=saveFile(idBack, uploadPath);
			String financialProofUrl=saveFile(financialProof, uploadPath);
			
			CardApplicationBean bean = new CardApplicationBean();
			bean.setUserId(member.getmId());
			bean.setCardType(cardType);
			bean.setIdPhotoFront(idFrontUrl);
			bean.setIdPhotoBack(idBackUrl);
			bean.setFinancialProof(financialProofUrl);
			bean.setApplyDate(LocalDateTime.now());
			
			int appId=cService.insertApplication(bean);
			
			return ResponseEntity.ok(Map.of("message","申請成功","applicationId",appId));
		}  catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error","申請失敗:"+e.getMessage()));
		}
	}
	
	@GetMapping("/record")
	public ResponseEntity<?> viewApplications(HttpSession session){
//		Member member=(Member) session.getAttribute("Member");
//		if(member==null) {
//			 return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                     .body(Map.of("error", "請先登入"));
//		}
		Member member = new Member();
		member.setmId(1);
		
		List<CardApplicationBean> entities = cService.findByUserId(member.getmId());

	    // 轉成 DTO List
	    List<CardApplicationDTO> dtos = entities.stream()
	        .map(e ->{
	            CardApplicationDTO dto = new CardApplicationDTO();
	            dto.setApplicationId(e.getApplicationId());
	            dto.setUserId(e.getUserId());
	            dto.setCardType(e.getCardType());
	            dto.setIdPhotoFront(e.getIdPhotoFront());
	            dto.setIdPhotoBack(e.getIdPhotoBack());
	            dto.setFinancialProof(e.getFinancialProof());
	            dto.setApplyDate(e.getApplyDate());
	            dto.setStatus(e.getStatus());
	            
	            Member m = e.getMember();
	            MemberDto mDto = new MemberDto();
	            mDto.setMName(m.getmName());
	            mDto.setMIdentity(m.getmIdentity());
	            mDto.setMBirthday(m.getmBirthday());
	            mDto.setMPhone(m.getmPhone());
	            mDto.setMAddress(m.getmAddress());

	            dto.setMember(mDto);
	            return dto;
	        } )
	        .collect(Collectors.toList());
		
		List<CardApplicationBean> lists = cService.findByUserId(member.getmId());
		Map<Integer, String> cardTypeMap = cTService.getCardTypeMap();
	    Map<String, Object> result = new HashMap<>();
	    result.put("applications", dtos);
	    result.put("cardTypeNames", cardTypeMap);
	    return ResponseEntity.ok(result);
	}
	
}
