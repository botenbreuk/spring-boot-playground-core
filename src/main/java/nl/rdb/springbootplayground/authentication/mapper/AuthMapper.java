package nl.rdb.springbootplayground.authentication.mapper;

import nl.rdb.springbootplayground.authentication.AuthResult;
import nl.rdb.springbootplayground.authentication.UserAuthResult;
import nl.rdb.springbootplayground.user.User;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuthMapper {

    AuthMapper INSTANCE = Mappers.getMapper(AuthMapper.class);

    default AuthResult userToAuthResult(User user) {
        UserAuthResult result = new UserAuthResult();
        result.authenticated = user != null;
        updateUserAuthResult(user, result);

        return new AuthResult(result);
    }

    @Mapping(target = "authenticated", ignore = true)
    void updateUserAuthResult(User user, @MappingTarget UserAuthResult result);
}
