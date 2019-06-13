package ledahu.springframework.recipies.services;

import ledahu.springframework.recipies.commands.RecipeCommand;
import ledahu.springframework.recipies.domain.Recipe;

import java.util.Set;

public interface RecipeService {
    Set<Recipe> getRecipes();

    Recipe findById(Long id);

    RecipeCommand saveRecipeCommand(RecipeCommand command);

}
