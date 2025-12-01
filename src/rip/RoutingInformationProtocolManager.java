package rip;

import interfaces.RoutingProtocolManagementInterface;
import interfaces.UnicastServiceUserInterface;

public class RoutingInformationProtocolManager implements RoutingProtocolManagementInterface, UnicastServiceUserInterface {

    @Override
    public void UPDataInd(short sendingEntity, String message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'UPDataInd'");
    }

    @Override
    public boolean getDistanceTable(short node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDistanceTable'");
    }

    @Override
    public boolean getLinkCost(short nodeA, short nodeB) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLinkCost'");
    }

    @Override
    public boolean setLinkCost(short nodeA, short nodeB, int cost) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setLinkCost'");
    }
    
}
