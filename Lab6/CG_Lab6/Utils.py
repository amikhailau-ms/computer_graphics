from PyQt5 import QtCore
from PyQt5.QtGui import QPen, QColor


def get_qpen():
    pen = QPen()
    pen.setWidth(1)
    pen.setStyle(QtCore.Qt.SolidLine)
    pen.setColor(QColor(51, 204, 255))
    pen.setCapStyle(QtCore.Qt.SquareCap)
    pen.setJoinStyle(QtCore.Qt.RoundJoin)
    return pen


def multiply_matvec(A, b):
    dimensions = len(A) - 1
    result = [0.0 for _ in range(dimensions)]
    for i in range(dimensions):
        for j in range(dimensions):
            result[i] += A[i][j] * b[j]
    for j in range(dimensions):
        result[j] += A[j][dimensions]
    return result


def multiply_matrixes(A, B):
    n = len(A)
    p = len(B)
    m = len(B[0])
    result = [[0.0 for _ in range(m)] for _ in range(n)]
    for i in range(n):
        for j in range(m):
            for k in range(p):
                result[i][j] += A[i][k] * B[k][j]
    return result
