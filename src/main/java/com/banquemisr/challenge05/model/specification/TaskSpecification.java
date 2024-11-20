package com.banquemisr.challenge05.model.specification;

import com.banquemisr.challenge05.model.Task;
import com.banquemisr.challenge05.model.criteria.TaskSearchCriteria;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TaskSpecification implements Specification<Task> {

    private static final Logger logger = Logger.getLogger(TaskSpecification.class.getName());
    private final TaskSearchCriteria criteria;

    public TaskSpecification(TaskSearchCriteria criteria) {
        if (criteria == null) {
            throw new IllegalArgumentException("TaskSearchCriteria must not be null");
        }
        this.criteria = criteria;
        logger.info("TaskSpecification initialized with criteria.");
    }
    @Override
    public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        logger.info("Building predicates for task search criteria.");
        List<Predicate> predicates = new ArrayList<>();

        if (isNonEmpty(criteria.getTitle())) {
            String title = criteria.getTitle().toLowerCase();
            logger.info(() -> String.format("Applying title filter: %s", title));
            predicates.add(builder.like(builder.lower(root.get("title")), "%" + title + "%"));
        } else {
            logger.info("Title filter not applied (null or empty).");
        }

        if (isNonEmpty(criteria.getDescription())) {
            String description = criteria.getDescription().toLowerCase();
            logger.info(() -> String.format("Applying description filter: %s", description));
            predicates.add(builder.like(builder.lower(root.get("description")), "%" + description + "%"));
        } else {
            logger.info("Description filter not applied (null or empty).");
        }

        if (criteria.getStatus() != null) {
            logger.info(() -> String.format("Applying status filter: %s", criteria.getStatus()));
            predicates.add(builder.equal(root.get("status"), criteria.getStatus()));
        } else {
            logger.info("Status filter not applied (null).");
        }

        if (criteria.getPriority() != null) {
            logger.info(() -> String.format("Applying priority filter: %s", criteria.getPriority()));
            predicates.add(builder.equal(root.get("priority"), criteria.getPriority()));
        }else {
            logger.info("Priority filter not applied (null).");
        }

        if (criteria.getDueDateFrom() != null) {
            logger.info(() -> String.format("Applying dueDateFrom filter: %s", criteria.getDueDateFrom()));
            predicates.add(builder.greaterThanOrEqualTo(root.get("dueDate"), criteria.getDueDateFrom()));
        }else {
            logger.info("DueDateFrom filter not applied (null).");
        }

        if (criteria.getDueDateTo() != null) {
            logger.info(() -> String.format("Applying dueDateTo filter: %s", criteria.getDueDateTo()));
            predicates.add(builder.lessThanOrEqualTo(root.get("dueDate"), criteria.getDueDateTo()));
        }else {
            logger.info("DueDateTo filter not applied (null).");
        }

        if (criteria.getAssignedUserId() != null) {
            logger.info("Applying assignedUserId filter.");
            predicates.add(builder.equal(root.join("assignedTo", JoinType.LEFT).get("id"), criteria.getAssignedUserId()));
        } else {
            logger.info("AssignedUserId filter not applied (null).");
        }

        logger.info(() -> String.format("Total filters applied: %d", predicates.size()));
        return builder.and(predicates.toArray(new Predicate[0]));
    }
    private boolean isNonEmpty(String value) {
        return value != null && !value.isEmpty();
    }
}