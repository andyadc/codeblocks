package com.andyadc.bms.web;

import com.andyadc.bms.annotation.Version;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/version")
@Controller
public class VersionController {

	@GetMapping("/{id}")
	public ResponseEntity<Object> version(@PathVariable Long id, @Version("1") String version) {
		return ResponseEntity.ok(version);
	}
}
