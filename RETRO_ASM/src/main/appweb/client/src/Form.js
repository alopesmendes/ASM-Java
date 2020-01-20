import React from 'react';
import './style.css';

function Form() {
  return (
    <div className="Form">
        <form><!-- Formulaire -->
                    <label for="Civilité">Civilité :</label>
                        <select name="Civilité" id="Civilité"><!--Ici on veut un bouton on ne prend pas un input mais <select>-->
                            <option value="Monsieur">Monsieur</option>
                            <option value="Madame">Madame</option>
                        </select> <br>
                    <label for="Nom">Nom : </label>
                        <input name="Nom" type="text" id="Nom"> <br>
                    <label for="Password">Mot de passe : </label>
                        <input name="Password" type="text" id="Password"><br>
                    <input type="submit" value="Envoyer">
        </form>
    </div>
  );
}

export default Form;