package beam.events;

import java.util.Map;

import beam.charging.vehicle.AgentChargingState;
import beam.transEnergySim.chargingInfrastructure.stationary.ChargingPlugStatus;
import javassist.expr.Instanceof;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.Event;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.api.internal.HasPersonId;

import beam.charging.vehicle.PlugInVehicleAgent;
import beam.transEnergySim.chargingInfrastructure.stationary.ChargingPlug;
import beam.transEnergySim.chargingInfrastructure.stationary.ChargingSite;

public class EndChargingSessionEvent extends Event implements IdentifiableDecisionEvent {

	private PlugInVehicleAgent agent;
	private ChargingPlug plug;
	private double soc;
	private int decisionEventId;
	private int numInChargingQueue;
	
	public static final String ATTRIBUTE_DECISION_EVENT_ID=DepartureChargingDecisionEvent.ATTRIBUTE_DECISION_EVENT_ID;
	public static final String ATTRIBUTE_PERSON = DepartureChargingDecisionEvent.ATTRIBUTE_PERSON;
	public static final String ATTRIBUTE_SOC=DepartureChargingDecisionEvent.ATTRIBUTE_SOC;	

	public EndChargingSessionEvent(double time, PlugInVehicleAgent agent, ChargingPlug plug) {
		super(time);
		this.agent = agent;
		this.soc = agent.getSoC()/agent.getBatteryCapacity();
		this.setDecisionEventId(agent.getCurrentDecisionEventId());
		this.plug = plug;
	}

	@Override
	public String getEventType() {
		return this.getClass().getSimpleName();
	}

	@Override
	public Map<String, String> getAttributes() {
		final Map<String, String> attributes = super.getAttributes();
		attributes.put(ATTRIBUTE_PERSON, agent.getPersonId().toString());
		attributes.put(ATTRIBUTE_SOC, Double.toString(soc));
		attributes.put(ATTRIBUTE_DECISION_EVENT_ID, Integer.toString(getDecisionEventId()));
		return attributes;
	}
	
	public Id<Person> getPersonId() {
		return this.agent.getPersonId();
	}

	public int getDecisionEventId() {
		return decisionEventId;
	}

	private void setDecisionEventId(int decisionEventId) {
		this.decisionEventId = decisionEventId;
	}
	
	public double getSoC() {
		return soc;
	}

	public AgentChargingState getChargingState(){
		return this.agent.getChargingState();
	}

	public int getNominalChargingLevel(){
		return this.plug.getChargingPlugType().getNominalLevel();
	}

	public boolean shouldDepartAfterChargingSession(){
		return this.agent.shouldDepartAfterChargingSession();
	}

	public int getNumInChargingQueue(){
		if(getNominalChargingLevel() >= 3) return this.plug.getChargingSite().getNumInChargingQueue(this.plug);
		else return this.plug.getChargingPoint().getNumInChargingQueue(this.plug);
	}

//	public int getNumInChargingQueue(){
//		return new Instanceof() this.agent.getSelectedChargingSite().qu;
//	}

}