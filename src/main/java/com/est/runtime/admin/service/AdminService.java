package com.est.runtime.admin.service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.est.runtime.admin.dto.AuthorityInfo;
import com.est.runtime.admin.dto.AuthorityRequestInfo;
import com.est.runtime.admin.dto.LogInfo;
import com.est.runtime.admin.dto.MemberInfo;
import com.est.runtime.admin.dto.UserLevelInfo;
import com.est.runtime.admin.dto.request.AdminAddAuthorityForLevelRequest;
import com.est.runtime.admin.dto.request.AdminAuthorityRequest;
import com.est.runtime.admin.dto.request.AdminCreateAuthorityRequest;
import com.est.runtime.admin.dto.request.AdminCreateUserLevelRequest;
import com.est.runtime.admin.dto.request.AdminUpdateMemberLevelRequest;
import com.est.runtime.admin.dto.response.AdminAuthorityRequestListResponse;
import com.est.runtime.admin.dto.response.AdminAuthorityResponse;
import com.est.runtime.admin.dto.response.AdminCreateAuthorityResponse;
import com.est.runtime.admin.dto.response.AdminCreateUserLevelResponse;
import com.est.runtime.admin.dto.response.AdminGetLogsResponse;
import com.est.runtime.admin.dto.response.AdminGetMembersResponse;
import com.est.runtime.admin.dto.response.AdminGetUserLevelResponse;
import com.est.runtime.admin.dto.response.AdminUpdateMemberResponse;
import com.est.runtime.admin.entity.PendingAdminAuthorityRequest;
import com.est.runtime.admin.repository.PendingAdminRequestRepository;
import com.est.runtime.signup.entity.AccessAuthority;
import com.est.runtime.signup.entity.AuthorityForLevel;
import com.est.runtime.signup.entity.LoginRequestLog;
import com.est.runtime.signup.entity.Member;
import com.est.runtime.signup.entity.UserLevel;
import com.est.runtime.signup.repository.AccessAuthorityRepository;
import com.est.runtime.signup.repository.AuthorityForLevelRepository;
import com.est.runtime.signup.repository.LoginRequestLogRepository;
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
    private final PendingAdminRequestRepository pendingAdminRequestRepository;
    private final LoginRequestLogRepository loginRequestLogRepository;

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

    public AdminGetLogsResponse getUserLogs(Long memberId, LocalDateTime before, LocalDateTime after) {
        if (memberId == -1L) {
            return AdminGetLogsResponse.builder().member(MemberInfo.emptyInstance()).
                currentConsecutiveFailedLoginCount(-1L).
                currentLoginCount(-1L).
                logs(List.of()).
                joinDate(LocalDateTime.now()).
                message("Please specify a member ID").
            responseCode(HttpStatus.BAD_REQUEST).build();
        }
        Optional<Member> memberQuery = memberRepository.findById(memberId);
        if (memberQuery.isEmpty()) {
            return AdminGetLogsResponse.builder().member(MemberInfo.emptyInstance()).
                currentConsecutiveFailedLoginCount(-1L).
                currentLoginCount(-1L).
                logs(List.of()).
                message("No member found with the ID that you specified").
                responseCode(HttpStatus.NOT_FOUND).build();
        }
        List<LoginRequestLog> logQuery;
        Member mem = memberQuery.get();
        if (before != null && after != null) {
            logQuery = loginRequestLogRepository.findAllByMemberAndLoginTimeBetween(mem, before, after); 
        } else if (before != null && after == null) {
            logQuery = loginRequestLogRepository.findAllByMemberAndLoginTimeBefore(mem, before);
        } else if (before == null && after != null) {
            logQuery = loginRequestLogRepository.findAllByMemberAndLoginTimeAfter(mem, after);
        } else {
            logQuery = loginRequestLogRepository.findAllByMember(mem);
        }
        MemberInfo memberInfo = MemberInfo.builder().
            id(mem.getId()).
            levelNo(mem.getLevel().getLevelNumber()).
            nickname(mem.getNickname()).
            username(mem.getUsername()).build();
        List<LogInfo> logs = logQuery.stream().map(x -> LogInfo.builder().
            timeStamp(x.getLoginTime()).
            ipAddress(x.getIpAddress()).
            userAgent(x.getUserAgent()).
            isSuccessful(x.isSuccessful()).build()).toList();
        return AdminGetLogsResponse.builder().member(memberInfo).logs(logs).
            currentConsecutiveFailedLoginCount(mem.getConsecutiveFailedLoginAttempts()).
            currentLoginCount(mem.getLoginCount()).
            joinDate(mem.getJoinDate()).
            message("Logs for the member that you requested.").
            responseCode(HttpStatus.OK).build();
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

    public int getRandom() {
        try {
            return SecureRandom.getInstance("NativePRNGNonBlocking").nextInt(Integer.MAX_VALUE);
        } catch (NoSuchAlgorithmException nsae) {
            return (int) Math.random() * 1000000;
        }
    }

    public AdminAuthorityRequestListResponse getPendingAdminRequests() {
        List<AuthorityRequestInfo> pendingRequests = pendingAdminRequestRepository.findAll().stream().map(x -> 
            AuthorityRequestInfo.builder().
                requestId(x.getId())
                .requestMemberId(x.getMemberId())
                .challengeCode(x.getChallenge())
                .requestTime(x.getRequestTime()).build()).toList();
        return AdminAuthorityRequestListResponse.builder().requests(pendingRequests)
            .message("List of admin requests that are pending for approval. Total count is: " + pendingRequests.size())
            .responseCode(HttpStatus.OK).build();
    }

    public AdminAuthorityResponse handleAuthorityRequest(AdminAuthorityRequest request) {
        org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger("AdminService_authorityAdd");
        Optional<Member> memberQuery = memberRepository.findById(request.getMemberId());
        if (memberQuery.isEmpty()) {
            return AdminAuthorityResponse.builder().message("The member ID that you specified is invalid " + request.getMemberId()).responseCode(HttpStatus.BAD_REQUEST).build();
        }
        Member mem = memberQuery.get();
        if (mem.isAdmin()) {
            return AdminAuthorityResponse.builder().message("Your account " + mem.getUsername() + "already has the admin authority!").responseCode(HttpStatus.OK).build();
        }
        List<PendingAdminAuthorityRequest> allPendingRequestsForMember = pendingAdminRequestRepository.findAllByMemberId(request.getMemberId());
        List<PendingAdminAuthorityRequest> validPendingRequests = allPendingRequestsForMember.
            stream().filter(x -> x.getRequestTime() + 600000L > System.currentTimeMillis()).toList();
        if (!request.getChallenge().isEmpty()) {
            for (PendingAdminAuthorityRequest req: validPendingRequests) {
                if (req.getChallenge().equalsIgnoreCase(request.getChallenge())) {
                    mem.setAdmin(true);
                    memberRepository.save(mem);
                    pendingAdminRequestRepository.deleteById(req.getId());
                    return AdminAuthorityResponse.builder().message("Admin Authority Granted For The User " + mem.getUsername() + "!").responseCode(HttpStatus.OK).build();
                }
            }
        }
        if (!validPendingRequests.isEmpty()) {
            return AdminAuthorityResponse.builder().message("An admin authority request code was already issued for your account." +  
                "Please respond back with the correct code. " + 
                "If you did not respond within 10 minutes, please contact the system administrator.").responseCode(HttpStatus.FORBIDDEN).build();
        }
        String challenge = String.valueOf(getRandom());
        PendingAdminAuthorityRequest r = pendingAdminRequestRepository.save(PendingAdminAuthorityRequest.builder().challenge(challenge).memberId(request.getMemberId()).requestTime(System.currentTimeMillis()).build());
        log.info("Admin challenge string for member: " + mem.getUsername() + " is " + challenge);
        log.info("The challenge expires on " + (r.getRequestTime() + 600000L));
        return AdminAuthorityResponse.builder().message("Please respond to the challenge within 10 minutes!").responseCode(HttpStatus.FORBIDDEN).build();
    }

    public AdminCreateAuthorityResponse createAuthority(AdminCreateAuthorityRequest request) {
        if (request.getName().toUpperCase(Locale.ROOT).contains("ADMIN")) {
            return AdminCreateAuthorityResponse.builder().authority(AuthorityInfo.builder().id(-1L).name("").description("").build()).
                message("Creating ADMIN authorities is not allowed.").
                responseCode(HttpStatus.FORBIDDEN).build();
        }
        return authorityRepository.findByName(request.getName()).map(x -> 
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
