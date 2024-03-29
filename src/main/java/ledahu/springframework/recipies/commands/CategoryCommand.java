package ledahu.springframework.recipies.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CategoryCommand {
    private Long id;
    private String description;
}
