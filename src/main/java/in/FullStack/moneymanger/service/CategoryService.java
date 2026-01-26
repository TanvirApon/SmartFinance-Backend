package in.FullStack.moneymanger.service;


import in.FullStack.moneymanger.dto.CategoryDTO;
import in.FullStack.moneymanger.entity.CategoryEntity;
import in.FullStack.moneymanger.entity.ProfileEntity;
import in.FullStack.moneymanger.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final ProfileService profileService;
    private final CategoryRepository categoryRepository;


    // save category
    public CategoryDTO saveCategory(CategoryDTO categoryDTO) {
        ProfileEntity profile = profileService.getCurrentProfile();

        if(categoryRepository.existsByNameAndProfileId(categoryDTO.getName(), profile.getId())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Category with ths name already exists");
        }
        CategoryEntity newCategory = toEntity(categoryDTO,profile);
        newCategory = categoryRepository.save(newCategory);
        return toDTO(newCategory);
    }

    // get categories for current user
    public List<CategoryDTO> getAllCategories(){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<CategoryEntity> categories = categoryRepository.findByProfileId(profile.getId());
        return categories.stream().map(this::toDTO).toList();
    }

    // get the categories by type for the current user
    public List<CategoryDTO>getCategoriesByTypeForCurrentUser(String type){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<CategoryEntity> optionalCategory = categoryRepository.findByTypeAndProfileId(type, profile.getId());

       return optionalCategory.stream().map(this::toDTO).toList();
    }

    //update the category
    public CategoryDTO updateCategory(Long categoryID,  CategoryDTO categoryDTO) {
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity category = categoryRepository.findByIdAndProfileId(categoryID, categoryDTO.getId())
                .orElseThrow(()-> new RuntimeException("Category not found "));

        category.setName(categoryDTO.getName());
        category.setIcon(categoryDTO.getIcon());
        categoryRepository.save(category);
        return toDTO(category);
    }

    // helper Methods
    private CategoryEntity toEntity(CategoryDTO categoryDTO, ProfileEntity profile) {

        return CategoryEntity.builder()
                .name(categoryDTO.getName())
                .icon(categoryDTO.getIcon())
                .profile(profile)
                .type(categoryDTO.getType())
                .build();
    }

    private CategoryDTO toDTO(CategoryEntity entity) {

        return CategoryDTO.builder()
                .id(entity.getId())
                .profileId(entity.getProfile()!=null?entity.getProfile().getId():null)
                .name(entity.getName())
                .icon(entity.getIcon())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .type(entity.getType())
                .build();
    }
}
