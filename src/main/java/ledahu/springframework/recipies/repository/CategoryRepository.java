package ledahu.springframework.recipies.repository;

import ledahu.springframework.recipies.domain.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {
}
