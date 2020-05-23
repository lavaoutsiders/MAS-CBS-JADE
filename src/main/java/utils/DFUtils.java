package utils;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DFUtils {


    public static List<AID> searchDF(String service, Agent agent)
    {
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType( service );
        dfd.addServices(sd);

        SearchConstraints ALL = new SearchConstraints();
        ALL.setMaxResults((long) -1);

        try
        {
            return Arrays.stream(DFService.search(agent, dfd, ALL))
                    .map((DFAgentDescription::getName))
                    .collect(Collectors.toList());

        }
        catch (FIPAException fe) { fe.printStackTrace(); }

        return null;
    }
}
