package com.finpilot.config;

import com.finpilot.entity.Category;
import com.finpilot.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryCleanupInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public void run(String... args) {
        List<Category> categories = categoryRepository.findAll();
        for (Category category : categories) {
            if ("Hacked Category".equalsIgnoreCase(category.getCategoryName())
                    && category.getUser() == null
                    && (category.getIsSystem() == null || !category.getIsSystem())) {
                Long catId = category.getCategoryId();

                jdbcTemplate.update("UPDATE transactions SET category_id = NULL WHERE category_id = ?", catId);
                jdbcTemplate.update("UPDATE recurring_transactions SET category_id = NULL WHERE category_id = ?", catId);
                jdbcTemplate.update("DELETE FROM budget_items WHERE category_id = ?", catId);

                categoryRepository.delete(category);
                System.out.println("Removed stale orphan category: " + category.getCategoryName() + " (ID: " + catId + ")");
            }
        }
    }
}
