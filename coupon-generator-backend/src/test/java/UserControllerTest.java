import org.example.controller.UserController;
import org.example.dto.ApiResponse;
import org.example.dto.UserDTO;
import org.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    UserService userService;

    @Mock
    UserDTO userDTO;

    @InjectMocks
    UserController userController;

    @Test
    void addUserTest(){
        when(userService.addUser(userDTO)).thenReturn(new ApiResponse());

        ApiResponse apiResponse = userController.addUser(userDTO);

        assertNotNull(apiResponse);
    }

    @Test
    void getAllUsersTest(){
        when(userService.getUsers()).thenReturn(new ApiResponse());

        ApiResponse apiResponse = userController.getAllUsers();

        assertNotNull(apiResponse);
    }

    @Test
    void updateUserTest(){
        int userId = 1;
        when(userService.updateUser(userDTO, userId)).thenReturn(new ApiResponse());

        ApiResponse apiResponse = userController.updateApp(userDTO, userId);

        assertNotNull(apiResponse);
    }

    @Test
    void deleteUserTest(){
        int userId = 1;
        when(userService.deleteUser(userId)).thenReturn(new ApiResponse());

        ApiResponse apiResponse = userController.deleteUser(userId);

        assertNotNull(apiResponse);
    }
}
