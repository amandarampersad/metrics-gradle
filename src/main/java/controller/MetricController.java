package controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Controller
public class MetricController {
    @Autowired
    private HashMap<String, List> metrics;

    /*
        Typical runtime: O(1)
        Worst case runtime: O(n)
        Space complexity: O(n)
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@RequestParam(name = "name", required = true) String name) {
        JSONObject toReturn = new JSONObject();

        try {
            validateName(name);
            ArrayList<Double> values = new ArrayList<>();
            metrics.put(name, values);
            toReturn.put("success", "Metric " + name + " created");
        } catch (Exception e) {
            toReturn.put("error", e.getLocalizedMessage());
        }

        return toReturn.toString();
    }

    /*
        Typical runtime: O(n * log(n)) because the metric is sorted
        with every insertion of a new value
        Space complexity: O(n)
     */
    @RequestMapping(value = "/addvalue", method = RequestMethod.POST)
    public String addValueToMetric(@RequestParam(name = "name", required = true) String name,
                                   @RequestParam(name = "value", required = true) Double value) {
        JSONObject toReturn = new JSONObject();

        try {
            checkIfMetricExists(name);
            ArrayList<Double> values = (ArrayList<Double>) metrics.get(name);
            values = insertValueInOrder(values, value);
            metrics.put(name, values);

            toReturn.put("success", "Value " + value + " inserted into metric " + name);
        } catch (Exception e) {
            toReturn.put("error", e.getLocalizedMessage());
        }

        return toReturn.toString();
    }

    /*
        Runtime: O(n)
        Space complexity: O(n)
     */
    @RequestMapping(value = "/getmean/{name}", method = RequestMethod.GET)
    public String getMean(@PathVariable String name) {
        JSONObject toReturn = new JSONObject();

        try {
            checkIfMetricExists(name);
            ArrayList<Double> values = (ArrayList<Double>) metrics.get(name);
            Double sum = 0.0;

            if (values.isEmpty()) {
                toReturn.put("error", "Metric " + name + " is empty");
                return toReturn.toString();
            }

            for (Double entry : values) {
                sum += entry;
            }

            Double mean = sum/values.size();
            toReturn.put("success", mean);
        } catch (Exception e) {
            toReturn.put("error", e.getLocalizedMessage());
        }

        return toReturn.toString();
    }

    /*
        Runtime: O(1)
        Worst case runtime: O(n)
        Space complexity: O(n)
     */
    @RequestMapping(value = "/getmedian/{name}", method = RequestMethod.GET)
    public String getMedian(@PathVariable String name) {
        JSONObject toReturn = new JSONObject();

        try {
            checkIfMetricExists(name);
            ArrayList<Double> values = (ArrayList<Double>) metrics.get(name);
            Double median;
            int size = values.size();

            if (values.isEmpty()) {
                toReturn.put("error", "Metric " + name + " is empty");
                return toReturn.toString();
            }

            if ((size % 2) > 0) {
                median = values.get(size/2);
            } else {
                // 0 1 2 3
                median = (values.get(size/2) + values.get((size/2) - 1)) / 2;
            }

            toReturn.put("success", Double.valueOf(median));
        } catch (Exception e) {
            toReturn.put("error", e.getLocalizedMessage());
        }

        return toReturn.toString();
    }

    /*
        Runtime: O(1)
        Worst case runtime: O(n)
        Space complexity: O(n)
     */
    @RequestMapping(value = "/getmin/{name}", method = RequestMethod.GET)
    public String getMin(@PathVariable String name) {
        JSONObject toReturn = new JSONObject();

        try {
            checkIfMetricExists(name);
            ArrayList<Double> values = (ArrayList<Double>) metrics.get(name);

            if (values.isEmpty()) {
                toReturn.put("error", "Metric " + name + " is empty");
                return toReturn.toString();
            }

            toReturn.put("success", values.get(0));
        } catch (Exception e) {
            toReturn.put("error", e.getLocalizedMessage());
        }

        return toReturn.toString();
    }

    /*
        Runtime: O(1)
        Worst case runtime: O(n)
        Space complexity: O(n)
     */
    @RequestMapping(value = "/getmax/{name}", method = RequestMethod.GET)
    public String getMax(@PathVariable String name) {
        JSONObject toReturn = new JSONObject();

        try {
            checkIfMetricExists(name);
            ArrayList<Double> values = (ArrayList<Double>) metrics.get(name);
            int size = values.size();

            if (values.isEmpty()) {
                toReturn.put("error", "Metric " + name + " is empty");
                return toReturn.toString();
            }

            toReturn.put("success", values.get(size - 1));
        } catch (Exception e) {
            toReturn.put("error", e.getLocalizedMessage());
        }

        return toReturn.toString();
    }

    private void validateName(String name) throws Exception {
        if (name == null || name.length() > 50 || name.length() == 0) {
            throw new Exception("Metric name is invalid");
        }

        if (metrics.containsKey(name)) {
            throw new Exception("Metric with " + name + "already exists");
        }
    }

    private void checkIfMetricExists(String name) throws Exception {
        if (name == null || name.length() == 0) {
            throw new Exception("Metric name is invalid");
        }

        if (!metrics.containsKey(name)) {
            throw new Exception("Metric with name " + name + " does not exist");
        }
    }

    private ArrayList insertValueInOrder(ArrayList values,
                                         Double value) throws Exception {

        if (value == null) {
            throw new Exception("Can't insert null value into metric");
        }

        values.add(value);
        Collections.sort(values);

        return values;
    }

    public void setMetrics(HashMap metrics) {
        this.metrics = metrics;
    }

    public HashMap getMetrics() {
        return metrics;
    }
}
