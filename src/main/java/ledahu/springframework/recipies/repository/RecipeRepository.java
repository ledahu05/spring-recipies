package ledahu.springframework.recipies.repository;

import ledahu.springframework.recipies.domain.Recipe;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {
}
