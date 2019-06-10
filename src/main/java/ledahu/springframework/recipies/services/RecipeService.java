package ledahu.springframework.recipies.services;

import ledahu.springframework.recipies.domain.Recipe;

import java.util.Set;

public interface RecipeService {
    Set<Recipe> getRecipes();
}
