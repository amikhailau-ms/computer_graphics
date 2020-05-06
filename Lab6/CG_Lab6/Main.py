import copy
import sys
import numpy as np
from math import cos, pi, sin

from PyQt5.QtGui import QDoubleValidator, QColor
from PyQt5.QtWidgets import QMainWindow, QApplication, QMessageBox
from pyqtgraph.opengl import MeshData, GLMeshItem, GLGridItem, GLAxisItem

from Utils import multiply_matrixes, multiply_matvec
from WindowUI import Ui_MainWindow


class MainWindow(QMainWindow, Ui_MainWindow):

    def __init__(self, *args, **kwargs):
        super(MainWindow, self).__init__(*args, **kwargs)
        self.setupUi(self)
        self.numberValidator = QDoubleValidator()
        self.translateX.setValidator(self.numberValidator)
        self.translateY.setValidator(self.numberValidator)
        self.translateZ.setValidator(self.numberValidator)
        self.scaleX.setValidator(self.numberValidator)
        self.scaleY.setValidator(self.numberValidator)
        self.scaleZ.setValidator(self.numberValidator)
        self.rotateX.setValidator(self.numberValidator)
        self.rotateY.setValidator(self.numberValidator)
        self.rotateZ.setValidator(self.numberValidator)
        self.projectionsVisible = False
        self.projectXY = None
        self.projectXZ = None
        self.projectYZ = None
        self.dim = 3
        self.setWindowTitle("3D Letter")
        self.vertexes = np.array([
            [20, -20, -10],
            [20, -20, 10],
            [10, -20, 10],
            [10, -20, -10],
            [20, 10, -10],
            [20, 10, 10],
            [10, 10, 10],
            [10, 10, -10],
            [20, 20, -10],
            [20, 20, 10],
            [10, 20, 10],
            [10, 20, -10],
            [0, 0, -10],
            [0, 10, -10],
            [0, 0, 10],
            [0, 10, 10],
            [-10, -20, -10],
            [-10, -20, 10],
            [-20, -20, 10],
            [-20, -20, -10],
            [-10, 10, -10],
            [-10, 10, 10],
            [-20, 10, 10],
            [-20, 10, -10],
            [-10, 20, -10],
            [-10, 20, 10],
            [-20, 20, 10],
            [-20, 20, -10]
        ])
        self.faces = np.array([
            [3, 0, 8],
            [8, 11, 3],
            [0, 1, 9],
            [9, 8, 0],
            [2, 3, 7],
            [7, 6, 2],
            [1, 2, 10],
            [10, 9, 1],
            [0, 3, 2],
            [2, 1, 0],
            [12, 7, 11],
            [11, 13, 12],
            [20, 12, 13],
            [13, 24, 20],
            [13, 11, 10],
            [10, 15, 13],
            [24, 13, 15],
            [15, 25, 24],
            [14, 21, 25],
            [25, 15, 14],
            [6, 14, 15],
            [15, 10, 6],
            [14, 6, 7],
            [7, 12, 14],
            [21, 14, 12],
            [12, 20, 21],
            [16, 17, 21],
            [21, 20, 16],
            [18, 19, 27],
            [27, 26, 18],
            [19, 16, 24],
            [24, 27, 19],
            [17, 18, 26],
            [26, 27, 17],
            [18, 17, 16],
            [16, 19, 18],
            [27, 24, 25],
            [25, 26, 27]
        ])
        self.original_vertexes = copy.deepcopy(self.vertexes)
        self.letter = GLMeshItem(meshdata=MeshData(self.vertexes, self.faces), drawFaces=False,
                                 edgeColor=QColor(51, 204, 255), drawEdges=True)
        self.pushButton.clicked.connect(self.perform_transformations)
        self.resetButton.clicked.connect(self.reset)
        self.projectionButton.clicked.connect(self.show_projections)
        self.init_plot()

    def init_plot(self):
        axis = GLAxisItem()
        axis.setSize(1000, 1000, 1000)
        self.widget.addItem(axis)
        xgrid = GLGridItem()
        ygrid = GLGridItem()
        zgrid = GLGridItem()

        xgrid.rotate(90, 0, 1, 0)
        ygrid.rotate(90, 1, 0, 0)
        xgrid.setSpacing(10, 10, 10)
        ygrid.setSpacing(10, 10, 10)
        zgrid.setSpacing(10, 10, 10)
        xgrid.setSize(2000, 2000, 2000)
        ygrid.setSize(2000, 2000, 2000)
        zgrid.setSize(2000, 2000, 2000)

        self.widget.addItem(xgrid)
        self.widget.addItem(ygrid)
        self.widget.addItem(zgrid)
        self.widget.addItem(self.letter)
        self.build_projections()

    def plot_letter(self):
        self.widget.removeItem(self.letter)
        self.letter = GLMeshItem(meshdata=MeshData(self.vertexes, self.faces), drawFaces=False,
                                 edgeColor=QColor(51, 204, 255), drawEdges=True)
        self.widget.addItem(self.letter)

    def perform_transformations(self):
        translationMatrix = self.get_translations()
        rotationMatrix = self.get_rotations()
        scalingMatrix = self.get_scaling()
        result = np.array(multiply_matrixes(multiply_matrixes(translationMatrix, rotationMatrix), scalingMatrix))
        QMessageBox.information(self, "Transformation matrix", "{0}".format(result))
        new_vertexes = []
        for vertex in self.vertexes:
            new_vertexes.append(multiply_matvec(result, vertex))
        self.vertexes = np.array(new_vertexes)
        print("NEW APPLIED!")
        self.build_projections()
        self.plot_letter()

    def get_translations(self):
        result = [[0.0 for _ in range(self.dim + 1)] for _ in range(self.dim + 1)]
        for i in range(self.dim + 1):
            result[i][i] = 1.0
        try:
            if self.translateX.text() != "":
                result[0][self.dim] = float(self.translateX.text())
            if self.translateY.text() != "":
                result[1][self.dim] = float(self.translateY.text())
            if self.translateZ.text() != "":
                result[2][self.dim] = float(self.translateZ.text())
        except ValueError:
            QMessageBox.warning(self, "Value error", "Some values in translation part are invalid.")
        return result

    def get_rotations(self):
        result = [[0.0 for _ in range(self.dim + 1)] for _ in range(self.dim + 1)]
        for i in range(self.dim + 1):
            result[i][i] = 1.0
        try:
            if self.rotateX.text() != "":
                resultX = [[0.0 for _ in range(self.dim + 1)] for _ in range(self.dim + 1)]
                for i in range(self.dim + 1):
                    resultX[i][i] = 1.0
                resultX[1][1] = cos(float(self.rotateX.text()) / 180 * pi)
                resultX[2][2] = cos(float(self.rotateX.text()) / 180 * pi)
                resultX[1][2] = -sin(float(self.rotateX.text()) / 180 * pi)
                resultX[2][1] = sin(float(self.rotateX.text()) / 180 * pi)
                result = multiply_matrixes(result, resultX)
            if self.rotateY.text() != "":
                resultY = [[0.0 for _ in range(self.dim + 1)] for _ in range(self.dim + 1)]
                for i in range(self.dim + 1):
                    resultY[i][i] = 1.0
                resultY[0][0] = cos(float(self.rotateY.text()) / 180 * pi)
                resultY[2][2] = cos(float(self.rotateY.text()) / 180 * pi)
                resultY[2][0] = -sin(float(self.rotateY.text()) / 180 * pi)
                resultY[0][2] = sin(float(self.rotateY.text()) / 180 * pi)
                result = multiply_matrixes(result, resultY)
            if self.rotateZ.text() != "":
                resultZ = [[0.0 for _ in range(self.dim + 1)] for _ in range(self.dim + 1)]
                for i in range(self.dim + 1):
                    resultZ[i][i] = 1.0
                resultZ[0][0] = cos(float(self.rotateZ.text()) / 180 * pi)
                resultZ[1][1] = cos(float(self.rotateZ.text()) / 180 * pi)
                resultZ[0][1] = -sin(float(self.rotateZ.text()) / 180 * pi)
                resultZ[1][0] = sin(float(self.rotateZ.text()) / 180 * pi)
                result = multiply_matrixes(result, resultZ)
        except ValueError:
            QMessageBox.warning(self, "Value error", "Some values in rotation part are invalid.")
        return result

    def get_scaling(self):
        result = [[0.0 for _ in range(self.dim + 1)] for _ in range(self.dim + 1)]
        for i in range(self.dim + 1):
            result[i][i] = 1.0
        try:
            if self.scaleX.text() != "":
                result[0][0] = float(self.scaleX.text())
            if self.scaleY.text() != "":
                result[1][1] = float(self.scaleY.text())
            if self.scaleZ.text() != "":
                result[2][2] = float(self.scaleZ.text())
            if self.scaleAll.text() != "":
                result[0][0] *= float(self.scaleAll.text())
                result[1][1] *= float(self.scaleAll.text())
                result[2][2] *= float(self.scaleAll.text())
        except ValueError:
            QMessageBox.warning(self, "Value error", "Some values in scaling part are invalid.")
        return result

    def reset(self):
        self.vertexes = copy.deepcopy(self.original_vertexes)
        self.build_projections()
        self.plot_letter()

    def build_projections(self):
        if self.projectXY is not None and self.projectionsVisible:
            self.widget.removeItem(self.projectXY)
        if self.projectXZ is not None and self.projectionsVisible:
            self.widget.removeItem(self.projectXZ)
        if self.projectYZ is not None and self.projectionsVisible:
            self.widget.removeItem(self.projectYZ)
        self.projectionsVisible = False
        projectXY = [[0.0 for _ in range(self.dim + 1)] for _ in range(self.dim + 1)]
        for i in range(self.dim + 1):
            projectXY[i][i] = 1.0
        projectYZ = copy.deepcopy(projectXY)
        projectXZ = copy.deepcopy(projectXY)
        projectXY[2][2] = 0.0
        projectXZ[1][1] = 0.0
        projectYZ[0][0] = 0.0
        projectXY_vert = []
        projectXZ_vert = []
        projectYZ_vert = []
        for vertex in self.vertexes:
            projectXY_vert.append(multiply_matvec(projectXY, vertex))
            projectXZ_vert.append(multiply_matvec(projectXZ, vertex))
            projectYZ_vert.append(multiply_matvec(projectYZ, vertex))
        self.projectXY = GLMeshItem(meshdata=MeshData(np.array(projectXY_vert), self.faces), drawFaces=True,
                                    edgeColor=QColor(9, 156, 19), drawEdges=False)
        self.projectXZ = GLMeshItem(meshdata=MeshData(np.array(projectXZ_vert), self.faces), drawFaces=True,
                                    edgeColor=QColor(9, 156, 19), drawEdges=False)
        self.projectYZ = GLMeshItem(meshdata=MeshData(np.array(projectYZ_vert), self.faces), drawFaces=True,
                                    edgeColor=QColor(9, 156, 19), drawEdges=False)

    def show_projections(self):
        if self.projectionsVisible:
            if self.projectXY is not None:
                self.widget.removeItem(self.projectXY)
            if self.projectXZ is not None:
                self.widget.removeItem(self.projectXZ)
            if self.projectYZ is not None:
                self.widget.removeItem(self.projectYZ)
            self.projectionsVisible = False
        else:
            if self.projectXY is not None:
                self.widget.addItem(self.projectXY)
            if self.projectXZ is not None:
                self.widget.addItem(self.projectXZ)
            if self.projectYZ is not None:
                self.widget.addItem(self.projectYZ)
            self.projectionsVisible = True


def main():
    app = QApplication(sys.argv)
    GUI = MainWindow()
    GUI.show()
    sys.exit(app.exec_())


main()
