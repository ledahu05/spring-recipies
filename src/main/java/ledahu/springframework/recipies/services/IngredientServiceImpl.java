package ledahu.springframework.recipies.services;

import ledahu.springframework.recipies.commands.IngredientCommand;
import ledahu.springframework.recipies.converters.IngredientCommandToIngredient;
import ledahu.springframework.recipies.converters.IngredientToIngredientCommand;
import ledahu.springframework.recipies.domain.Ingredient;
import ledahu.springframework.recipies.domain.Recipe;
import ledahu.springframework.recipies.repository.RecipeRepository;
import ledahu.springframework.recipies.repository.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {
    private final RecipeRepository recipeRepository;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand,
                                 IngredientCommandToIngredient ingredientCommandToIngredient,
                                 RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
        Optional<Recipe> recipe = recipeRepository.findById(recipeId);
        if(recipe.isPresent()) {
            Ingredient ingredient = recipe.get()
                    .getIngredients()
                    .stream()
                    .filter(i -> i.getId() == ingredientId)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Ingredient not found"));
            return ingredientToIngredientCommand.convert(ingredient);
        } else {
            throw new RuntimeException("Recipe not found");
        }

    }

    @Override
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {
        Recipe recipe = recipeRepository.findById(command.getRecipeId())
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        Optional<Ingredient> optIngredient = recipe.getIngredients()
                .stream()
                .filter(i -> i.getId() == command.getId())
                .findFirst();

        if(optIngredient.isPresent()) {
            Ingredient ingredientFound = optIngredient.get();
            ingredientFound.setAmount(command.getAmount());
            ingredientFound.setUom(unitOfMeasureRepository
                    .findById(command.getUom().getId())
                    .orElseThrow(() -> new RuntimeException("UOM NOT FOUND"))); //todo address this
        } else {
            //add new Ingredient
            Ingredient ingredient = ingredientCommandToIngredient.convert(command);
            ingredient.setRecipe(recipe);
            recipe.addIngredient(ingredient);
        }

        Recipe savedRecipe = recipeRepository.save(recipe);

        Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
                .filter(recipeIngredients -> recipeIngredients.getId().equals(command.getId()))
                .findFirst();

        //check by description
        if(!savedIngredientOptional.isPresent()){
            //not totally safe... But best guess
            savedIngredientOptional = savedRecipe.getIngredients().stream()
                    .filter(recipeIngredients -> recipeIngredients.getDescription().equals(command.getDescription()))
                    .filter(recipeIngredients -> recipeIngredients.getAmount().equals(command.getAmount()))
                    .filter(recipeIngredients -> recipeIngredients.getUom().getId().equals(command.getUom().getId()))
                    .findFirst();
        }

        return ingredientToIngredientCommand.convert(savedIngredientOptional.get());

    }

    @Override
    public void deleteByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
        Optional<Recipe> optRecipe = recipeRepository.findById(recipeId);
        if(optRecipe.isPresent()) {
            Recipe recipe = optRecipe.get();
            Ingredient ingredient = recipe.getIngredients()
                    .stream().filter(i -> i.getId() == ingredientId)
                    .findFirst().orElseThrow(() -> new RuntimeException("Ingredient not found"));

            ingredient.setRecipe(null);

            log.debug(recipe.getIngredients().size() + "ingredients before delete");
            recipe.getIngredients().removeIf(i -> i.getId() == ingredientId);
            log.debug(recipe.getIngredients().size() + "ingredients after delete");

            recipeRepository.save(recipe);

        } else {
            throw new RuntimeException("Recipe not found");
        }
    }
}
