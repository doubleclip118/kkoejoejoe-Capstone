package Capstone.Capstone.utils.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserRegistrationException.class)
    public ResponseEntity<?> handleUserRegistrationException(UserRegistrationException ex,WebRequest request){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.CONFLICT);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex,WebRequest request){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AWSCloudInfoNotFoundException.class)
    public ResponseEntity<?> handleAWSCloudInfoNotFoundException(AWSCloudInfoNotFoundException ex,WebRequest request){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AzureCloudInfoNotFoundException.class)
    public ResponseEntity<?> handleAzureCloudInfoNotFoundException(AzureCloudInfoNotFoundException ex,WebRequest request){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CbSpiderServerException.class)
    public ResponseEntity<?> handleCbSpiderServerException(CbSpiderServerException ex,WebRequest request){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.GATEWAY_TIMEOUT);
    }

    @ExceptionHandler(CloudInfoIncorrectException.class)
    public ResponseEntity<?> handleCloudInfoIncorrectException(CloudInfoIncorrectException ex,WebRequest request){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(VmInfoNotFoundException.class)
    public ResponseEntity<?> handleVmInfoNotFoundException(VmInfoNotFoundException ex,WebRequest request){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OpenStackCloudInfoNotFoundException.class)
    public ResponseEntity<?> handleOpenStackCloudInfoNotFoundException(OpenStackCloudInfoNotFoundException ex,WebRequest request){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NetworkNotFoundException.class)
    public ResponseEntity<?> handleNetworkNotFoundExceptionException(NetworkNotFoundException ex,WebRequest request){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }

}
