package com.est.runtime.admin.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.est.runtime.admin.dto.AuthorityInfo;
import com.est.runtime.admin.dto.MemberInfo;
import com.est.runtime.admin.dto.UserLevelInfo;
import com.est.runtime.admin.dto.request.AdminAddAuthorityForLevelRequest;
import com.est.runtime.admin.dto.request.AdminCreateAuthorityRequest;
import com.est.runtime.admin.dto.request.AdminCreateUserLevelRequest;
import com.est.runtime.admin.dto.request.AdminUpdateMemberLevelRequest;
import com.est.runtime.admin.dto.response.AdminCreateAuthorityResponse;
import com.est.runtime.admin.dto.response.AdminCreateUserLevelResponse;
import com.est.runtime.admin.dto.response.AdminGetMembersResponse;
import com.est.runtime.admin.dto.response.AdminGetUserLevelResponse;
import com.est.runtime.admin.dto.response.AdminUpdateMemberResponse;
import com.est.runtime.signup.entity.AccessAuthority;
import com.est.runtime.signup.entity.AuthorityForLevel;
import com.est.runtime.signup.entity.Member;
import com.est.runtime.signup.entity.UserLevel;
import com.est.runtime.signup.repository.AccessAuthorityRepository;
import com.est.runtime.signup.repository.AuthorityForLevelRepository;
import com.est.runtime.signup.repository.MemberRepository;
import com.est.runtime.signup.repository.UserLevelRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AdminService {
    private final MemberRepository memberRepository;
    private final UserLevelRepository userLevelRepository;
    private final AccessAuthorityRepository authorityRepository;
    private final AuthorityForLevelRepository authorityForLevelRepository;

    public AdminGetMembersResponse getAllMembers() {
        List<MemberInfo> memberList = memberRepository.findAll().
            stream().
            map(x -> MemberInfo.builder().id(x.getId()).nickname(x.getNickname()).username(x.getUsername()).levelNo(x.getLevel().getLevelNumber()).build()).
            toList();
        if (memberList.isEmpty()) {
            return AdminGetMembersResponse.builder().members(memberList).responseCode(HttpStatus.NOT_FOUND).message("No members were found!").build();
        }
        return AdminGetMembersResponse.builder().members(memberList).responseCode(HttpStatus.OK).message("List of all members.").build();
    }

    public AdminGetMembersResponse getMemberById(Long id) {
        Optional<Member> memberQuery = memberRepository.findById(id);
        if (memberQuery.isEmpty()) {
            return AdminGetMembersResponse.builder().members(List.of()).responseCode(HttpStatus.NOT_FOUND).message("No member with the ID" + id + " was found!").build();
        }
        Member mem = memberQuery.get();
        return AdminGetMembersResponse.builder().members(List.of(MemberInfo.builder().
            id(mem.getId()).
            nickname(mem.getNickname()).
            username(mem.getUsername()).levelNo(mem.getLevel().getLevelNumber()).build())).responseCode(HttpStatus.OK).message("Member found.").build();
    }

    public AdminGetMembersResponse getMembersContainingNicknameAndUsername(String nickname, String username) {
        List<MemberInfo> memberList = memberRepository.searchMembers(nickname, username).
            stream().
            map(x -> MemberInfo.builder().id(x.getId()).nickname(x.getNickname()).username(x.getUsername()).levelNo(x.getLevel().getLevelNumber()).build()).
            toList();
        if (memberList.isEmpty()) {
            return AdminGetMembersResponse.builder().members(memberList).responseCode(HttpStatus.NOT_FOUND).message("No members were found!").build();
        }
        return AdminGetMembersResponse.builder().members(memberList).responseCode(HttpStatus.OK).message("List of members that match your query.").build();
    }

    public AdminUpdateMemberResponse updateMemberLevel(AdminUpdateMemberLevelRequest request) {
        Optional<Member> memberQuery = memberRepository.findById(request.getMemberId());
        Optional<UserLevel> userLevelQuery = userLevelRepository.findByLevelNumber(request.getLevelNum());
        if (memberQuery.isEmpty()) {
            return AdminUpdateMemberResponse.builder().
                member(MemberInfo.builder().
                    id(-1L).
                    levelNo(-1).
                    nickname("null").
                    username("null").build()).
                message("The user ID that you requested (" + request.getMemberId() + ") is invalid!").
                responseCode(HttpStatus.NOT_FOUND).build();
        }
        Member mem = memberQuery.get();
        if (userLevelQuery.isEmpty()) {
            return AdminUpdateMemberResponse.builder().
                member(MemberInfo.builder().
                    id(mem.getId()).
                    levelNo(mem.getLevel().getLevelNumber()).
                    nickname(mem.getNickname()).
                    username(mem.getUsername()).build()).
                message("The level number that you requested (" + request.getLevelNum() + ") is invalid!").
                responseCode(HttpStatus.NOT_FOUND).build();
        }
        UserLevel level = userLevelQuery.get();

        mem.setLevel(level);
        memberRepository.save(mem);
        return AdminUpdateMemberResponse.builder().
            member(MemberInfo.builder().
                id(mem.getId()).
                levelNo(mem.getLevel().getLevelNumber()).
                nickname(mem.getNickname()).
                username(mem.getUsername()).build()).
            message("Level updated").
            responseCode(HttpStatus.OK).build();
    }

    public AdminCreateAuthorityResponse createAuthority(AdminCreateAuthorityRequest request) {
        return  authorityRepository.findByName(request.getName()).map(x -> 
            AdminCreateAuthorityResponse.builder().
                authority(AuthorityInfo.builder().id(x.getId()).name(x.getName()).description(x.getDescription()).build()).
                message("An authority with the name that you specified already exists!").
                responseCode(HttpStatus.CONFLICT).
            build()).
        orElseGet(() -> {
            AccessAuthority authority = authorityRepository.save(AccessAuthority.builder().
                name(request.getName()).
                description(request.getDescription()).build());
            return AdminCreateAuthorityResponse.builder().
                authority(AuthorityInfo.builder().id(authority.getId()).name(authority.getName()).description(authority.getDescription()).build()).
                message("Authority Created!").
                responseCode(HttpStatus.OK).build();
        });
    }

    public AdminCreateUserLevelResponse removeAuthorityForLevel(AdminAddAuthorityForLevelRequest request) {
        if (request.getAuthorityId() == -1L && request.getAuthorityName().isEmpty()) {
            return AdminCreateUserLevelResponse.builder().
                level(UserLevelInfo.emptyInstance()).
                message("Please specify either authority ID or name!").
                responseCode(HttpStatus.BAD_REQUEST).build();
        }
        Optional<AccessAuthority> authorityQuery = authorityRepository.findById(request.getAuthorityId()).
            or(() -> authorityRepository.findByName(request.getAuthorityName()));
        if (authorityQuery.isEmpty()) {
            return AdminCreateUserLevelResponse.builder().
                level(UserLevelInfo.emptyInstance()).
                message("Unable to find authority by the ID or name that you specified!").
                responseCode(HttpStatus.BAD_REQUEST).build();
        }
        Optional<UserLevel> levelQuery = userLevelRepository.findByLevelNumber(request.getLevelNumber());
        if (levelQuery.isEmpty()) {
            return AdminCreateUserLevelResponse.builder().
                level(UserLevelInfo.emptyInstance()).
                message("The level number that you specified doesn't exist").
                responseCode(HttpStatus.NOT_FOUND).
            build();
        }

        AccessAuthority authority = authorityQuery.get();
        UserLevel level = levelQuery.get();
        List<AccessAuthority> authoritiesForLevel = authorityRepository.findAllByLevelId(level.getId());

        for (AccessAuthority al: authoritiesForLevel) {
            if (al.getId() == authority.getId()) {
                authorityForLevelRepository.deleteAuthorityForLevel(level.getId(), al.getId());
                return AdminCreateUserLevelResponse.builder().
                    level(UserLevelInfo.builder().
                        levelName(level.getLevelName()).
                        levelNo(level.getLevelNumber()).
                        id(level.getId()).
                        authorities(authorityRepository.findAllByLevelId(level.getId())
                            .stream().map(x -> AuthorityInfo.builder().
                            id(x.getId()).
                            name(x.getName()).
                            description(x.getDescription()).
                            build()).
                        toList()).build()).
                    message("Authority revoked").
                    responseCode(HttpStatus.OK).
                build();
            }
        }

        return AdminCreateUserLevelResponse.builder().
            level(UserLevelInfo.builder().
            levelName(level.getLevelName()).
            levelNo(level.getLevelNumber()).
            id(level.getId()).
            authorities(authoritiesForLevel.stream().map(x -> AuthorityInfo.builder().
                            id(x.getId()).
                            name(x.getName()).
                            description(x.getDescription()).
                            build()).
                        toList()).build()).
                        message("THe level that you specified doesn't have the authority").
                        responseCode(HttpStatus.NOT_FOUND).build();
    }

    public AdminGetUserLevelResponse getUserLevels() {
        List<UserLevelInfo> infos = userLevelRepository.findAll().stream().map(x -> 
            UserLevelInfo.builder().
            levelName(x.getLevelName()).
            levelNo(x.getLevelNumber()).
            id(x.getId()).
            authorities(x.getAuthorities().stream().map(y -> AuthorityInfo.builder().
                id(y.getAuthority().getId()).
                name(y.getAuthority().getName()).
                description(y.getAuthority().getDescription()).build()).toList()).build()).toList();
        return AdminGetUserLevelResponse.builder().levels(infos).message("List of levels." + infos.size() + " found.").responseCode(HttpStatus.OK).build();
    }

    public AdminCreateUserLevelResponse addAuthorityForUserLevel(AdminAddAuthorityForLevelRequest request) {
        if (request.getAuthorityId() == -1L && request.getAuthorityName().isEmpty()) {
            return AdminCreateUserLevelResponse.builder().
                level(UserLevelInfo.emptyInstance()).
                message("Please specify either authority ID or name!").
                responseCode(HttpStatus.BAD_REQUEST).build();
        }
        Optional<AccessAuthority> authorityQuery = authorityRepository.findById(request.getAuthorityId()).
            or(() -> authorityRepository.findByName(request.getAuthorityName()));
        if (authorityQuery.isEmpty()) {
            return AdminCreateUserLevelResponse.builder().
                level(UserLevelInfo.emptyInstance()).
                message("Unable to find authority by the ID or name that you specified!").
                responseCode(HttpStatus.BAD_REQUEST).build();
        }
        Optional<UserLevel> levelQuery = userLevelRepository.findByLevelNumber(request.getLevelNumber());
        if (levelQuery.isEmpty()) {
            return AdminCreateUserLevelResponse.builder().
                level(UserLevelInfo.emptyInstance()).
                message("The level number that you specified doesn't exist").
                responseCode(HttpStatus.NOT_FOUND).
            build();
        }
        AccessAuthority authority = authorityQuery.get();
        UserLevel level = levelQuery.get();
        List<AccessAuthority> authoritiesForLevel = authorityRepository.findAllByLevelId(level.getId());

        for (AccessAuthority al: authoritiesForLevel) {
            if (al.getId() == authority.getId()) {
                return AdminCreateUserLevelResponse.builder().
                    level(UserLevelInfo.builder().
                        levelName(level.getLevelName()).
                        levelNo(level.getLevelNumber()).
                        id(level.getId()).
                        authorities(authoritiesForLevel.stream().map(x -> AuthorityInfo.builder().
                            id(x.getId()).
                            name(x.getName()).
                            description(x.getDescription()).
                            build()).
                        toList()).build()).
                    message("The level that you specified already has the authority that you requested to grant").
                    responseCode(HttpStatus.CONFLICT).
                build();
            }
        }

        authorityForLevelRepository.save(AuthorityForLevel.builder().authority(authority).userLevel(level).build());
        authoritiesForLevel = authorityRepository.findAllByLevelId(level.getId());

        return AdminCreateUserLevelResponse.builder().
            level(UserLevelInfo.builder().
            levelName(level.getLevelName()).
            levelNo(level.getLevelNumber()).
            id(level.getId()).
            authorities(authoritiesForLevel.stream().map(x -> AuthorityInfo.builder().
                            id(x.getId()).
                            name(x.getName()).
                            description(x.getDescription()).
                            build()).
                        toList()).build()).
                        message("Authority granted for the level").
                        responseCode(HttpStatus.OK).build();
    }

    public AdminCreateUserLevelResponse createNewLevel(AdminCreateUserLevelRequest request) {
        return userLevelRepository.
            findByLevelName(request.getLevelName()).map(x -> 
            AdminCreateUserLevelResponse.builder().
                    level(UserLevelInfo.builder().
                        levelName(x.getLevelName()).
                        levelNo(x.getLevelNumber()).
                        authorities(
                            x.getAuthorities().stream().map(a -> 
                                AuthorityInfo.builder().
                                    id(a.getAuthority().getId()).
                                    name(a.getAuthority().getName()).
                                    description(a.getAuthority().getDescription()).build()).
                            toList()
                        ).build()).
                    message("Level name already exists!").
                    responseCode(HttpStatus.CONFLICT).build()).
        or(() -> userLevelRepository.
            findByLevelNumber(request.getLevelNumber()).map(x -> 
                AdminCreateUserLevelResponse.builder().
                    level(UserLevelInfo.builder().
                        levelName(x.getLevelName()).
                        levelNo(x.getLevelNumber()).
                        authorities(
                            x.getAuthorities().stream().map(a -> 
                                AuthorityInfo.builder().
                                    id(a.getAuthority().getId()).
                                    name(a.getAuthority().getName()).
                                    description(a.getAuthority().getDescription()).build()).
                            toList()
                    ).build()).
                message("Level number already exists!").
                responseCode(HttpStatus.CONFLICT).build())).
            orElseGet(() -> {
                UserLevel lvl = userLevelRepository.save(UserLevel.builder().
                    levelName(request.getLevelName()).
                    levelNumber(request.getLevelNumber()).build());
                return AdminCreateUserLevelResponse.builder().
                    level(UserLevelInfo.builder().
                        levelName(lvl.getLevelName()).
                        levelNo(lvl.getLevelNumber()).
                        authorities(
                            lvl.getAuthorities().stream().map(a -> 
                                AuthorityInfo.builder().
                                    id(a.getAuthority().getId()).
                                    name(a.getAuthority().getName()).
                                    description(a.getAuthority().getDescription()).build()).
                            toList()
                        ).build()).
                    message("New user level created!").
                    responseCode(HttpStatus.OK).build();
                });
    }



}
