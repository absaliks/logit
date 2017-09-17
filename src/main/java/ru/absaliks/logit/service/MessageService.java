package ru.absaliks.logit.service;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.ObjectUtils;

@XmlRootElement
public class MessageService {

  private static final Logger LOG = Logger.getLogger(MessageService.class.getName());

  @XmlElement private List<Message> messages;

  private transient Map<String, Message> simpleMessages;
  private transient List<Message> regexMessages;

  public MessageService() {
    this.simpleMessages = new HashMap<>();
    this.regexMessages = new ArrayList<>();
    LOG.severe(Integer.toString(messages.size()));
  }

  public String translate(String engMessage) {
    Message message = simpleMessages.get(engMessage);
    if (isNull(message)) {
      message = findInRegexMessages(engMessage);
    }
    return defaultIfNull(message.rus, null);
  }

  private Message findInRegexMessages(String engMessage) {
    for (Message msg : regexMessages) {
      try {
        if (getEngPattern(msg).matcher(engMessage).matches()) {
          return msg;
        }
      } catch (PatternSyntaxException e) {
        LOG.log(Level.WARNING, "Unable to compile regex pattern, removing message " + msg, e);
        regexMessages.remove(msg);
      }
    }
    return null;
  }

  private Pattern getEngPattern(Message msg) {
    return isNull(msg.engPattern) ? msg.engPattern = Pattern.compile(msg.eng) : msg.engPattern;
  }

  class Message {
    @XmlElement String eng;
    @XmlElement String rus;
    @XmlAttribute boolean isRegex;
    @XmlAttribute boolean isParametrized;
    transient Pattern engPattern;

    @Override
    public String toString() {
      return "Message{" +
          "eng='" + eng + '\'' +
          ", rus='" + rus + '\'' +
          ", isRegex=" + isRegex +
          ", isParametrized=" + isParametrized +
          '}';
    }
  }
}
