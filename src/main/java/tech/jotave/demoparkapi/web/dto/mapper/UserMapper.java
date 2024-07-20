package tech.jotave.demoparkapi.web.dto.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import tech.jotave.demoparkapi.entity.User;
import tech.jotave.demoparkapi.web.dto.UserCreateDto;
import tech.jotave.demoparkapi.web.dto.UserResponseDto;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    public static User toUser(UserCreateDto userCreateDto) {
        return new ModelMapper().map(userCreateDto, User.class);
    }

    public static UserResponseDto toDto(User user) {
        String role = user.getRole().name().substring("ROLE_".length());

        PropertyMap<User, UserResponseDto> props = new PropertyMap<User, UserResponseDto>() {
            @Override
            protected void configure() {
                map().setRole(role);
            }
        };

        ModelMapper mapper = new ModelMapper();
        mapper.addMappings(props);

        return mapper.map(user, UserResponseDto.class);
    }

    public static List<UserResponseDto> toListDto(List<User> users) {
        return users.stream().map(user -> toDto(user)).collect(Collectors.toList());
    }
}
