package com.dentistarchive.job;

import com.sksoldev.rep.actor.client.http.RoleClient;
import com.sksoldev.rep.actor.dto.create.RoleCreateDto;
import com.sksoldev.rep.actor.dto.create.RolesCreateDto;
import com.sksoldev.rep.actor.dto.enums.ActorMsPermission;
import com.sksoldev.rep.actor.dto.enums.ActorScope;
import com.sksoldev.rep.announcement.dto.AnnouncementMsPermission;
import com.sksoldev.rep.application.dto.ApplicationMsPermission;
import com.sksoldev.rep.data.loader.app.constants.Locales;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DefaultRolesCreationJob {

    public static final String SUPER_ADMIN_ROLE_CODE = "super-admin";
    public static final String INTERNAL_MS_CLIENT_ROLE_CODE = "internal-ms-client";
    public static final String USUAL_USER_ROLE_CODE = "usual-user";
    public static final String INDIVIDUAL_BROKER_ROLE_CODE = "broker";
    public static final String BROKER_COMPANY_MANAGER_ROLE_CODE = "broker-company-manager";
    public static final String BROKER_COMPANY_AGENT_ROLE_CODE = "broker-company-agent";
    public static final String CONSTRUCTION_COMPANY_MANAGER_ROLE_CODE = "construction-company-manager";
    public static final String PHOTOGRAPHER_ROLE_CODE = "photographer";
    public static final String ASSESSMENT_EXPERT_ROLE_CODE = "assessment-expert";

    public static final Set<RoleCreateDto> DEFAULT_ROLES = Set.of(

            RoleCreateDto.builder()
                    .code(SUPER_ADMIN_ROLE_CODE)
                    .clientRole(false)
                    .scope(ActorScope.ADMIN)
                    .namesMap(Map.of(
                            Locales.EN, "Super administrator",
                            Locales.RU, "Супер администратор",
                            Locales.UZ, "Super ma'mur"
                    ))
                    .descriptionsMap(Map.of(
                            Locales.EN, "Has all admin permissions",
                            Locales.RU, "Имеет все права администратора",
                            Locales.UZ, "Barcha ma'muriy ruxsatlarga ega"
                    ))
                    .permissionCodes(Set.of(
                            ActorMsPermission.READ_ALL_USERS.getCode(),
                            ApplicationMsPermission.READ_ALL_APPLICATIONS.getCode(),
                            ActorMsPermission.MANAGE_SERVICE_PROVIDERS.getCode(),
                            ApplicationMsPermission.MODERATE_ALL_APPLICATIONS.getCode(),
                            AnnouncementMsPermission.MODERATE_ALL_ANNOUNCEMENTS.getCode()
                    ))
                    .build(),

            RoleCreateDto.builder()
                    .code(INTERNAL_MS_CLIENT_ROLE_CODE)
                    .clientRole(true)
                    .scope(ActorScope.SYSTEM)
                    .namesMap(Map.of(
                            Locales.EN, "Internal client for microservices",
                            Locales.RU, "Внутренний учетная запись для микросервисов",
                            Locales.UZ, "Mikroxizmatlar uchun ichki hisob"
                    ))
                    .descriptionsMap(Map.of(
                            Locales.EN, "Has all permissions that are needed for microservices",
                            Locales.RU, "Имеет все права которые нужны для микросервисов",
                            Locales.UZ, "Mikroxizmatlar uchun barcha huquqlarga ega"
                    ))
                    .permissionCodes(Set.of())
                    .build(),

            RoleCreateDto.builder()
                    .code(INDIVIDUAL_BROKER_ROLE_CODE)
                    .clientRole(false)
                    .scope(ActorScope.BROKER)
                    .namesMap(Map.of(
                            Locales.EN, "Broker",
                            Locales.RU, "Брокер",
                            Locales.UZ, "Broker"
                    ))
                    .descriptionsMap(Map.of(
                            Locales.EN, "Has all broker permissions",
                            Locales.RU, "Имеет все права брокера",
                            Locales.UZ, "Brokerning barcha huquqlari mavjud"
                    ))
                    .permissionCodes(Set.of())
                    .build(),

            RoleCreateDto.builder()
                    .code(BROKER_COMPANY_MANAGER_ROLE_CODE)
                    .clientRole(false)
                    .scope(ActorScope.BROKER)
                    .namesMap(Map.of(
                            Locales.EN, "Broker company manager",
                            Locales.RU, "Менеджер брокерской компании",
                            Locales.UZ, "Brokerlik kompaniyasi menejeri"
                    ))
                    .descriptionsMap(Map.of(
                            Locales.EN, "Has all permissions inside broker company",
                            Locales.RU, "Имеет все права внутри брокерской компании",
                            Locales.UZ, "Brokerlik kompaniyasi doirasida barcha huquqlarga ega"
                    ))
                    .permissionCodes(Set.of(
                            ApplicationMsPermission.ASSIGN_BROKERS_INSIDE_COMPANY.getCode()
                    ))
                    .build(),

            RoleCreateDto.builder()
                    .code(BROKER_COMPANY_AGENT_ROLE_CODE)
                    .clientRole(false)
                    .scope(ActorScope.BROKER)
                    .namesMap(Map.of(
                            Locales.EN, "Broker company agent",
                            Locales.RU, "Агент брокерской компании",
                            Locales.UZ, "Brokerlik agenti"
                    ))
                    .descriptionsMap(Map.of(
                            Locales.EN, "Can work with clients of broker company",
                            Locales.RU, "Может работать с клиентами брокерской компании",
                            Locales.UZ, "Brokerlik kompaniyasi mijozlari bilan ishlashi mumkin"
                    ))
                    .permissionCodes(Set.of())
                    .build(),

            RoleCreateDto.builder()
                    .code(CONSTRUCTION_COMPANY_MANAGER_ROLE_CODE)
                    .clientRole(false)
                    .scope(ActorScope.CONSTRUCTION)
                    .namesMap(Map.of(
                            Locales.EN, "Construction company manager",
                            Locales.RU, "Менеджер строительной компании",
                            Locales.UZ, "Qurilish kompaniyasi menejeri"
                    ))
                    .descriptionsMap(Map.of(
                            Locales.EN, "Has all permissions inside construction company",
                            Locales.RU, "Имеет все права внутри строительной компании",
                            Locales.UZ, "Qurilish kompaniyasida barcha huquqlarga ega"
                    ))
                    .permissionCodes(Set.of())
                    .build(),

            RoleCreateDto.builder()
                    .code(USUAL_USER_ROLE_CODE)
                    .clientRole(false)
                    .scope(ActorScope.USUAL)
                    .namesMap(Map.of(
                            Locales.EN, "Usual user",
                            Locales.RU, "Обычный пользователь",
                            Locales.UZ, "Odatdagi foydalanuvchi"
                    ))
                    .descriptionsMap(Map.of(
                            Locales.EN, "Can publish announcements and respond to published ones of other users",
                            Locales.RU, "Может публиковать объявления и откликаться на опубликованные объявления других пользователей",
                            Locales.UZ, "Reklamalarni joylashtirishi va boshqa foydalanuvchilar tomonidan e'lon qilingan reklamalarga javob berishi mumkin"
                    ))
                    .permissionCodes(Set.of(
                            ApplicationMsPermission.CREATE_ANNOUNCEMENT_APPLICATION.getCode()
                    ))
                    .build(),

            RoleCreateDto.builder()
                    .code(PHOTOGRAPHER_ROLE_CODE)
                    .clientRole(false)
                    .scope(ActorScope.SERVICE_PROVIDER)
                    .namesMap(Map.of(
                            Locales.EN, "Photographer",
                            Locales.RU, "Фотограф",
                            Locales.UZ, "Fotograf"
                    ))
                    .descriptionsMap(Map.of(
                            Locales.EN, "Service provider - photographer",
                            Locales.RU, "Поставщик услуг - фотограф",
                            Locales.UZ, "Xizmat ko'rsatuvchi - fotograf"
                    ))
                    .permissionCodes(Set.of())
                    .build(),

            RoleCreateDto.builder()
                    .code(ASSESSMENT_EXPERT_ROLE_CODE)
                    .clientRole(false)
                    .scope(ActorScope.SERVICE_PROVIDER)
                    .namesMap(Map.of(
                            Locales.EN, "Assessment expert",
                            Locales.RU, "Оценщик",
                            Locales.UZ, "Baholovchi"
                    ))
                    .descriptionsMap(Map.of(
                            Locales.EN, "Service provider - assessment expert",
                            Locales.RU, "Поставщик услуг - оценщик",
                            Locales.UZ, "Xizmat ko'rsatuvchi - baholovchi"
                    ))
                    .permissionCodes(Set.of())
                    .build()

    );

    final RoleClient roleClient;

    @Getter
    UUID superAdminRoleId;

    @Getter
    UUID internalMsClientRoleId;

    public void createDefaultRoles() {
        log.info("Default roles loading...");
        var roles = roleClient.overwriteAllRolesWithoutAccessControl(new RolesCreateDto(DEFAULT_ROLES));
        this.superAdminRoleId = roles.stream()
                .filter(it -> it.getCode().equals(SUPER_ADMIN_ROLE_CODE))
                .toList()
                .getFirst()
                .getId();
        this.internalMsClientRoleId = roles.stream()
                .filter(it -> it.getCode().equals(INTERNAL_MS_CLIENT_ROLE_CODE))
                .toList()
                .getFirst()
                .getId();
        log.info("Default roles loaded");
    }
}
