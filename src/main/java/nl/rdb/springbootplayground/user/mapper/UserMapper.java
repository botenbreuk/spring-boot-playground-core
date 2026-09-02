package nl.rdb.springbootplayground.user.mapper;

import nl.rdb.springbootplayground.user.User;
import nl.rdb.springbootplayground.user.UserResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserResult userToUserResult(User user);
}
