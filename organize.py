#!/usr/bin/env python3
import os
import shutil

frontend_dir_original = "frontEndFlightOnTime"
frontend_dir_nuevo = "frontend"
backend_dir = "backend"

script_nombre = os.path.basename(__file__) if "__file__" in globals() else None

def encontrar_frontend():
    for item in os.listdir('.'):
        if item.lower() == frontend_dir_original.lower() and os.path.isdir(item):
            return item
    return None

def organizar_proyecto():
    print("Iniciando la organización del proyecto...")

    frontend_real = encontrar_frontend()
    if frontend_real:
        shutil.move(frontend_real, frontend_dir_nuevo)
        print(f"Directorio '{frontend_real}' renombrado a '{frontend_dir_nuevo}'.")

    if not os.path.isdir(backend_dir):
        os.makedirs(backend_dir)
        print(f"Directorio '{backend_dir}' creado.")

    items_raiz = os.listdir('.')

    elementos_protegidos = {frontend_dir_nuevo, backend_dir}
    if script_nombre:
        elementos_protegidos.add(script_nombre)

    items_a_mover = [i for i in items_raiz if i not in elementos_protegidos]

    for item in items_a_mover:
        try:
            shutil.move(item, os.path.join(backend_dir, item))
            print(f"- Moviendo '{item}'")
        except Exception as e:
            print(f"- No se pudo mover '{item}': {e}")

    print("Organización completada.")

if __name__ == "__main__":
    organizar_proyecto()
