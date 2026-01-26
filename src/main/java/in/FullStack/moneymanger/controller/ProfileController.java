package in.FullStack.moneymanger.controller;

import in.FullStack.moneymanger.dto.AuthDTO;
import in.FullStack.moneymanger.dto.ProfileDTO;
import in.FullStack.moneymanger.entity.ProfileEntity;
import in.FullStack.moneymanger.repository.ProfileRepository;
import in.FullStack.moneymanger.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final ProfileRepository profileRepository;

    @PostMapping("/register")
    public ResponseEntity<ProfileDTO> responseProfileDTO(@RequestBody  ProfileDTO profileDTO) {
        ProfileDTO registerProfile =  profileService.registerProfile(profileDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerProfile);
    }

    @GetMapping("/activate")
    public ResponseEntity<String>activateProfile(@RequestParam String token) {
        boolean isActivated = profileService.activateProfile(token);
        if(isActivated) {
            return ResponseEntity.status(HttpStatus.OK).body("Profile Activated Successfully");
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Activation Token Not Valid");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>>login(@RequestBody AuthDTO authDTO) {
         try{
             // Debug: check DB user & password
             ProfileEntity p = profileRepository.findByEmail(authDTO.getEmail()).orElse(null);
             if (p == null) {
                 System.out.println("User not found");
             } else {
                 System.out.println("User found, password: " + p.getPassword() + ", active: " + p.getIsActive());
             }

             if(!profileService.isAccountActive(authDTO.getEmail())) {
                 return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message","Please Activate your account First "));
             }
             Map<String,Object> response = profileService.authicateAndGenerateToken(authDTO);
             return ResponseEntity.ok(response);
         } catch (Exception e) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message",e.getMessage()));
         }
    }
}
