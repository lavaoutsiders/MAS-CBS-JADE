package models;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import org.jetbrains.annotations.NotNull;

public class MessageContentMatcher implements MessageTemplate.MatchExpression {

    private Class<?> contentClass;

    public MessageContentMatcher(@NotNull Class<?> isClassOf) {
        this.contentClass = isClassOf;
    }

    @Override
    public boolean match(ACLMessage aclMessage) {
        try {
            return contentClass.isInstance(aclMessage.getContentObject());
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
        return false;
    }

    public MessageTemplate getMessageTemplate() {
        return new MessageTemplate(this);
    }

}
