package ledahu.springframework.recipies.services;

import ledahu.springframework.recipies.commands.IngredientCommand;

public interface IngredientService {
    IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId);

    IngredientCommand saveIngredientCommand(IngredientCommand command);

    void deleteByRecipeIdAndIngredientId(Long recipeId, Long ingredientId);
}
