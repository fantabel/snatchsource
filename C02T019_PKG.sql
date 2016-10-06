CREATE OR REPLACE PACKAGE "C02T019_PKG" IS
/*****************************************************************************************************************
    Nom de la composante : C02T019_PKG
    Nom de l'analyste    : Jason Wong
    Nom du programmeur   : Martin Normand
    Date de création     : 2012-02-15

    But : Cette unité de tâche permet d'obtenir le type d'agent (secteur) à savoir DSCA ou DGP
          Est utilisée par les 2 écrans de confirmations scolaires ad-hoc C02020105F et C02020109F.
/****************************************************************************************************************/

FUNCTION AGENT_EXCEPT_FCT (PVA_CD_UTIL VARCHAR2, PVA_SECTR VARCHAR2 DEFAULT 'A') RETURN BOOLEAN;

FUNCTION OBTNR_TYPE_AGENT_FCT (PVA_CD_UTIL VARCHAR2, PVA_SECTR VARCHAR2 DEFAULT 'A') RETURN VARCHAR2;

END C02T019_PKG; 
/


CREATE OR REPLACE PACKAGE BODY "C02T019_PKG" IS

/**********************************************************************************************************************

    Historique Des Modifications:

    Personne                Date         Commentaire et nom de la procédure modifiée
    ---------               -----------  -----------------------------------------------------------------------------------------
    
    Martin Normand          2012-02-15   Création du package
    Michel Dessureault      2014-10-17   DS-23025 - Agrandissement de la variable VVA_LISTE_PILOTE dans AGENT_EXCEPT_FCT.
    Dany Lecompte           2016-09-12   DS 24891 - Appeler SECAP pour obtenir agent d'exception AGENT_EXCEPT_FCT - GSA01V03_PROFIL_UTIL_PROFIL
                                                    OBTNR_TYPE_AGENT_FCT - AJOUT UPPER
    
**********************************************************************************************************************/

/*****************************************************************************************************************

  Nom de la fonction : AGENT_EXCEPT_FCT

  Nom du programmeur et la date de création : Martin Normand - 2012-02-15

  But : Cette procédure détermine si l'agent est un pilote du secteur reçu en paramètre à savoir (A)ttribution ou (P)rêt.
        Si le code de secteur n'est pas reçu on assume que la demande est pour l'attribution.
        Les pilotes de secteurs sont inscrits dans une table REFER afin de permettre de les changés au besoin.

  Paramètre d'entrée : PVA_CD_UTIL   : Code de l'utilisateur
                       PVA_SECTR     : (A)ttribution ou (P)rêt

  Paramètre sortie :   BOOLEEN

/****************************************************************************************************************/

FUNCTION AGENT_EXCEPT_FCT (PVA_CD_UTIL VARCHAR2, PVA_SECTR VARCHAR2 DEFAULT 'A') RETURN BOOLEAN IS
  

  VVA_EXISTS VARCHAR2(1);
  
  CURSOR CUR_AGENT_EXCEPT IS 
     SELECT 1 
       FROM GSA01V03_PROFIL_UTIL_PROFIL GSA
      WHERE UPPER(GSA.CD_PROFIL)   = UPPER('PILOTCRS'||PVA_SECTR)
        AND UPPER(GSA.CD_UTIL)     = UPPER(PVA_CD_UTIL)
        AND SYSDATE BETWEEN GSA.DT_DEBUT_ACTIV AND NVL(GSA.DT_FIN_ACTIV,SYSDATE); 

BEGIN
  
  OPEN CUR_AGENT_EXCEPT;
  FETCH CUR_AGENT_EXCEPT INTO VVA_EXISTS;
  CLOSE CUR_AGENT_EXCEPT;
  
  IF VVA_EXISTS IS NOT NULL THEN
     RETURN TRUE;
  
  ELSE
     RETURN FALSE;
  
  END IF;

END AGENT_EXCEPT_FCT;

FUNCTION OBTNR_TYPE_AGENT_FCT (PVA_CD_UTIL VARCHAR2, PVA_SECTR VARCHAR2 DEFAULT 'A') RETURN VARCHAR2 IS

/*****************************************************************************************************************

  Nom de la fonction : OBTNR_TYPE_AGENT_FCT

  Nom du programmeur et la date de création : Martin Normand - 2012-02-15

  But : Cette procédure détermine si l'agent est un agent d'un établissement d'enseignement ou 
        ou agent de la DSCA ou de la DGP ou pilote du secteur reçu en paramètre à savoir (A)ttribution ou (P)rêt.
        Si le code de secteur n'est pas reçu on assume que la demande est pour l'attribution.

  Paramètre d'entrée : PVA_CD_UTIL   : Code de l'utilisateur
                       PVA_SECTR     : (A)ttribution ou (P)rêt

  Paramètre sortie :   retourne 'EE' ou DSCA' ou 'DGP'

 Est appelée par les 2 écrans de confirmation des renseignements scolaires
 C02020105F appel avec PVA_SECTR='A' car est utilisé par l'attribution (DSCA) et
 C02020109F appel avec PVA_SECTR='P' car est par la gestion des prêts (DGP)

/****************************************************************************************************************/

CURSOR C_OBTNR_TYPE_AGENT IS
SELECT TYPE_UTIL,
       SERVC_NOTE
  FROM GSA01V02_IDENT_UTIL
 WHERE UPPER(CD_UTIL) = UPPER(PVA_CD_UTIL);

VVA_TYPE_UTIL  GSA01V02_IDENT_UTIL.TYPE_UTIL%TYPE;
VVA_SERVC_NOTE GSA01V02_IDENT_UTIL.SERVC_NOTE%TYPE;

VVA_TYPE_AGENT VARCHAR2(6);

BEGIN

  OPEN  C_OBTNR_TYPE_AGENT;
  FETCH C_OBTNR_TYPE_AGENT INTO VVA_TYPE_UTIL, VVA_SERVC_NOTE;
  CLOSE C_OBTNR_TYPE_AGENT;

  IF VVA_TYPE_UTIL = 'EE' THEN
     VVA_TYPE_AGENT := 'EE';
  ELSE
     -- Attribution
     IF PVA_SECTR='A' THEN
        --  Agent d'exception de l'attribution? (pilote DSCA)
        IF AGENT_EXCEPT_FCT(PVA_CD_UTIL, PVA_SECTR) THEN
           VVA_TYPE_AGENT := 'DSCA';
        -- Trouver type agent AFE
        ELSIF SUBSTR(VVA_SERVC_NOTE,1,4) IN('3431','3432','3433','3434') THEN
            VVA_TYPE_AGENT := 'DSCA';
        END IF;
     -- Pret
     ELSIF PVA_SECTR='P' THEN
        -- Agent d'exception du secteru prêts? (pilote DGP)
        IF AGENT_EXCEPT_FCT(PVA_CD_UTIL, PVA_SECTR) THEN
           VVA_TYPE_AGENT := 'DGP';
        ELSIF SUBSTR(VVA_SERVC_NOTE,1,4) IN('3441','3442','3443','3444','3445') THEN
          VVA_TYPE_AGENT := 'DGP';
        END IF;
    END IF;
  END IF;
// Test comment
  IF VVA_TYPE_AGENT IS NULL THEN
     c00t001_pkg.log_erreur_99_prc('C02T019_PKG.OBTNR_TYPE_AGENT_FCT', 'Impossible d''obtenir le type d agent pour CD_UTIL='||PVA_CD_UTIL||' et SECTEUR='||PVA_SECTR, null);
  END IF;

  RETURN VVA_TYPE_AGENT;

END OBTNR_TYPE_AGENT_FCT;

END C02T019_PKG;
/
