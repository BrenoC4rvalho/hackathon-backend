package com.hackathon.backend.service;

import com.hackathon.backend.dto.GroupRequest;
import com.hackathon.backend.dto.GroupResponse;
import com.hackathon.backend.entity.Group;
import com.hackathon.backend.exception.GroupNameAlreadyExistsException;
import com.hackathon.backend.exception.GroupNotFoundException;
import com.hackathon.backend.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    public GroupResponse create(GroupRequest request) {
        if (groupRepository.existsByName(request.name())) {
            throw new GroupNameAlreadyExistsException(request.name());
        }

        Group group = Group.builder()
                .name(request.name())
                .description(request.description())
                .build();

        return toResponse(groupRepository.save(group));
    }

    public List<GroupResponse> findAll() {
        return groupRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public GroupResponse findById(Long id) {
        return toResponse(getEntityById(id));
    }

    public GroupResponse update(Long id, GroupRequest request) {
        Group group = getEntityById(id);

        group.setName(request.name());
        group.setDescription(request.description());

        return toResponse(groupRepository.save(group));
    }

    public void delete(Long id) {
        Group group = getEntityById(id);
        groupRepository.delete(group);
    }

    public Group getEntityById(Long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException(id));
    }

    private GroupResponse toResponse(Group group) {
        return new GroupResponse(
                group.getId(),
                group.getName(),
                group.getDescription()
        );
    }
}