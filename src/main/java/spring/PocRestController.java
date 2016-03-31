package spring;

import org.springframework.web.bind.annotation.*;
import vo.PocVo;

import java.util.Arrays;
import java.util.List;

/**
 * Created by pgallazzi on 31/3/16.
 */
@RestController
public class PocRestController {

    //This is a POC to use the QUERY STRING params
    @RequestMapping(value = "/punga", method = RequestMethod.GET)
    public PocVo greeting(@RequestParam(value = "id", required = false, defaultValue = "1") String id) {
        return getPunga(Integer.valueOf(id));
    }

    //This is a POC to use the PATH params
    @RequestMapping(value = "/punga/{id}", method = RequestMethod.GET)
    public PocVo greeting(@PathVariable Integer id) {
        return getPunga(id);
    }

    PocVo getPunga(Integer id) {
        List<String> pungaHobbies = Arrays.asList("dormir", "comer", "nada");
        PocVo pungaInstance = new PocVo(1, "Pablo", "Gallazzi", 23, pungaHobbies);
        PocVo pungaInstance2 = new PocVo(2, "Nico", "Garcia", 23, pungaHobbies);
        List<PocVo> pungaDb = Arrays.asList(pungaInstance, pungaInstance2);
        for (PocVo punga : pungaDb) {
            if (punga.getId() == id) {
                return punga;
            }
        }
        return null;
    }

}
