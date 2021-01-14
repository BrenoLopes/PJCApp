package br.balladesh.pjcappbackend.controllers.security.signup;

import br.balladesh.pjcappbackend.utilities.commands.Command;
import br.balladesh.pjcappbackend.utilities.errors.BadRequestException;
import br.balladesh.pjcappbackend.utilities.errors.HttpException;
import br.balladesh.pjcappbackend.utilities.errors.InternalServerErrorException;
import br.balladesh.pjcappbackend.utilities.Result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.regex.Pattern;

public class CheckEmailCommand implements Command<Boolean, HttpException> {
  private static final Logger logger = LoggerFactory.getLogger(CheckEmailCommand.class);

  private final String email;

  public CheckEmailCommand(String email) {
    if (email == null)
      this.email = "";
    else
      this.email = email;
  }

  @Override
  public Result<Boolean, HttpException> execute() {
    Optional<Boolean> isEmailValid = this.isEmailValid(this.email);

    if (!isEmailValid.isPresent()) {
      String message = "An error occurred in the server while trying to parse the request!";
      return Result.fromError(new InternalServerErrorException(message));
    }

    if(!isEmailValid.get()) {
      String message = "The request could not proceed because the username is an invalid email address!";
      return Result.fromError(new BadRequestException(message));
    }

    return Result.from(true);
  }

  private Optional<Boolean> isEmailValid(String email) {
    try {
      String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
      Pattern pattern = Pattern.compile(regex);

      return Optional.of(pattern.matcher(email).matches());
    } catch (Exception e) {
      logger.error("CheckEmailCommand::isEmailValid Could not check if the email was valid!");
      return Optional.empty();
    }
  }
}
