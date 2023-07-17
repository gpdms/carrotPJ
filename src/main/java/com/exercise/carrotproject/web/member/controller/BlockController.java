package com.exercise.carrotproject.web.member.controller;

import com.exercise.carrotproject.domain.member.dto.MyBlockDto;
import com.exercise.carrotproject.domain.member.service.BlockService;
import com.exercise.carrotproject.web.member.error.ErrorCode;
import com.exercise.carrotproject.web.member.form.memberInfo.BlockForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/blocks")
@RequiredArgsConstructor
public class BlockController {
    private final BlockService blockService;

    @PostMapping
    public ResponseEntity block(@RequestBody final BlockForm form){
        boolean hasBlock = blockService.hasBlockByFromMemToMem(form.getFromMemId(), form.getToMemId());
        if(hasBlock) {
            return new ResponseEntity<>(ErrorCode.EXISTS_BLOCK, HttpStatus.CONFLICT);
        }
        blockService.insertBlock(form.getFromMemId(), form.getToMemId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity cancelBlock(@RequestBody final BlockForm form){
        blockService.deleteBlock(form.getFromMemId(), form.getToMemId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{memId}")
    public String myBlocks(@PathVariable final String memId, Model model){
        List<MyBlockDto> myBlocks = blockService.getMyBlocks(memId);
        model.addAttribute("myBlocks", myBlocks);
        return "myPage/blockList";
    }
}
