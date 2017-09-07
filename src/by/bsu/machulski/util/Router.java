package by.bsu.machulski.util;

import by.bsu.machulski.constant.PageConfigConstant;
import by.bsu.machulski.resource.ConfigurationManager;
import by.bsu.machulski.type.RoutingType;

public class Router {
    private RoutingType routingType;
    private String path;

    public Router() {
    }

    public Router(RoutingType routingType, String path) {
        this.routingType = routingType;
        this.path = path;
    }

    public RoutingType getRoutingType() {
        return routingType;
    }

    public void setRoutingType(RoutingType routingType) {
        this.routingType = routingType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
