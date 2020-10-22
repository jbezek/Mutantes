package com.mercadolibre.mutantes.app;

import com.mercadolibre.mutantes.app.model.documents.Human;
import com.mercadolibre.mutantes.app.vo.HumanValueObject;

public class MutantTranslator {

	private static MutantTranslator instance = null;

    public static MutantTranslator getInstance() {
        if (instance == null) {
            instance = new MutantTranslator();
        }
        return instance;
    }
    
//	public Human getPersistentObject(HumanValueObject MutantVO) {
//		Human human = new Mutant();
//		human.setId(MutantVO.getId());
//		human.setDireccion(MutantVO.getDireccion());
//		human.setLatitud(MutantVO.getLatitud());
//		human.setLongitud(MutantVO.getLongitud());
//		return human;
//	}
	
	public HumanValueObject getValueObject(Human human) {
		HumanValueObject humanVO = new HumanValueObject();
		humanVO.setId(human.getId());
		humanVO.setIsMutant(human.getMutant());
		humanVO.setDna(human.getDna());
		return humanVO;
	}
}
