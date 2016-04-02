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
    @RequestMapping(value = "/poc", method = RequestMethod.GET)
    public PocVo greeting(@RequestParam(value = "id", required = false, defaultValue = "1") String id) {
        return getPoc(Integer.valueOf(id));
    }

    //This is a POC to use the PATH params
    @RequestMapping(value = "/poc/{id}", method = RequestMethod.GET)
    public PocVo greeting(@PathVariable Integer id) {
        return getPoc(id);
    }

    PocVo getPoc(Integer id) {
        List<String> pocHobbies = Arrays.asList("dormir", "comer", "nada");
        PocVo pocInstance = new PocVo(1, "Pablo", "Gallazzi", 23, pocHobbies);
        PocVo pocInstance2 = new PocVo(2, "Nico", "Garcia", 23, pocHobbies);
        List<PocVo> pocDb = Arrays.asList(pocInstance, pocInstance2);
        for (PocVo poc : pocDb) {
            if (poc.getId() == id) {
                return poc;
            }
        }
        return null;
    }

}
