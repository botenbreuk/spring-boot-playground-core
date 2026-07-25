package nl.rdb.springbootplayground.architecture;

public enum ViolationType {
    PUBLIC_PRE_POST_AUTHORIZE, // allPublicServiceMethodsMustHavePreAuthorize
    SERVICE_TRANSACTIONAL, // ensureServiceIsTransactional
    ENTITY_BASE_CLASS, // entitiesMustExtendAbstractEntity
    SECURITY_UTIL_NAMING, // securityUtilAnnotatedClassesMustFollowNamingConvention
    REPOSITORY_BASE_CLASS, // repositoriesMustExtendCustomJpaRepository
    NONE
}
