package rip;

import interfaces.RoutingProtocolManagementInterface;
import interfaces.UnicastServiceInterface;

public class RoutingInformationProtocolNode implements UnicastServiceInterface, RoutingProtocolManagementInterface {

    @Override
    public void UPDataReq(short destinationEntity, String message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'UPDataReq'");
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
