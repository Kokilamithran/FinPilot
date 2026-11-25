package com.finpilot.controller;

import com.finpilot.dto.request.GoalContributionRequest;
import com.finpilot.dto.response.GoalContributionResponse;
import com.finpilot.entity.FinancialGoal;
import com.finpilot.entity.GoalContribution;
import com.finpilot.entity.User;
import com.finpilot.exception.ResourceNotFoundException;
import com.finpilot.mapper.GoalContributionMapper;
import com.finpilot.repository.FinancialGoalRepository;
import com.finpilot.security.SecurityUtils;
import com.finpilot.service.GoalContributionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/goal-contributions")
public class GoalContributionController {

    private final GoalContributionService service;
    private final FinancialGoalRepository financialGoalRepository;
    private final SecurityUtils securityUtils;

    public GoalContributionController(GoalContributionService service,
                                       FinancialGoalRepository financialGoalRepository,
                                       SecurityUtils securityUtils) {
        this.service = service;
        this.financialGoalRepository = financialGoalRepository;
        this.securityUtils = securityUtils;
    }

    @PostMapping
    public GoalContributionResponse createContribution(@Valid @RequestBody GoalContributionRequest request) {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        FinancialGoal goal;
        if (isAdmin) {
            goal = financialGoalRepository.findById(request.getGoalId())
                    .orElseThrow(() -> new ResourceNotFoundException("Financial Goal not found"));
        } else {
            goal = financialGoalRepository.findByGoalIdAndUser_UserId(request.getGoalId(), currentUser.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Financial Goal not found"));
        }

        GoalContribution contribution = GoalContributionMapper.toEntity(request, goal);

        return GoalContributionMapper.toResponse(
                service.createContribution(contribution)
        );
    }

    @GetMapping
    public List<GoalContributionResponse> getAllContributions() {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        return service.getAllContributions(currentUser.getUserId(), isAdmin)
                .stream()
                .map(GoalContributionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public GoalContributionResponse getContributionById(@PathVariable Long id) {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        return GoalContributionMapper.toResponse(
                service.getContributionById(id, currentUser.getUserId(), isAdmin)
        );
    }

    @PutMapping("/{id}")
    public GoalContributionResponse updateContribution(
            @PathVariable Long id,
            @Valid @RequestBody GoalContributionRequest request) {

        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        FinancialGoal goal;
        if (isAdmin) {
            goal = financialGoalRepository.findById(request.getGoalId())
                    .orElseThrow(() -> new ResourceNotFoundException("Financial Goal not found"));
        } else {
            goal = financialGoalRepository.findByGoalIdAndUser_UserId(request.getGoalId(), currentUser.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Financial Goal not found"));
        }

        GoalContribution contribution = GoalContributionMapper.toEntity(
                request,
                goal
        );

        return GoalContributionMapper.toResponse(
                service.updateContribution(id, contribution, currentUser.getUserId(), isAdmin)
        );
    }

    @DeleteMapping("/{id}")
    public String deleteContribution(@PathVariable Long id) {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        service.deleteContribution(id, currentUser.getUserId(), isAdmin);
        return "Goal Contribution deleted successfully";
    }
}